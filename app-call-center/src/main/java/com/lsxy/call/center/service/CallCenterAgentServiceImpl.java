package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.operations.AgentSkillOperationDTO;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.dao.CallCenterAgentDao;
import com.lsxy.call.center.api.states.lock.AgentLock;
import com.lsxy.call.center.api.states.lock.ExtensionLock;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.call.center.api.states.statics.ACs;
import com.lsxy.call.center.api.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.call.center.utils.Lua;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhangxb on 2016/10/21.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterAgentServiceImpl extends AbstractService<CallCenterAgent> implements CallCenterAgentService {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterAgentServiceImpl.class);

    @Autowired
    private CallCenterAgentDao callCenterAgentDao;
    @Autowired
    private AgentSkillService agentSkillService;
    @Autowired
    private AppExtensionService appExtensionService;
    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ConditionService conditionService;
    @Autowired
    private ExtensionState extensionState;
    @Autowired
    private AgentState agentState;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    private ACs aCs;
    @Autowired
    private CAs cAs;
    @Autowired
    private AgentActionLogService agentActionLogService;
    @Autowired
    private EnQueueService enQueueService;


    @Override
    public BaseDaoInterface<CallCenterAgent, Serializable> getDao() {
        return callCenterAgentDao;
    }


    /**
     * 座席和技能是临时数据，所以删除采用物理删除
     * @param id
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void delete(Serializable id) throws IllegalAccessException, InvocationTargetException {
        CallCenterAgent one = callCenterAgentDao.findOne(id);
        callCenterAgentDao.delete(one);
    }

    /**
     * 座席和技能是临时数据，所以删除采用物理删除
     * @param entity
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true)
    @Override
    public void delete(CallCenterAgent entity) throws IllegalAccessException, InvocationTargetException {
        callCenterAgentDao.delete(entity);
    }

    //登陆
    @Override
    public String login(CallCenterAgent agent) throws YunhuniApiException {
        //座席不能为空
        if(StringUtils.isBlank(agent.getName())){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("agent",agent)
            );
        }
        //初始化座席状态
        if(StringUtils.isBlank(agent.getState())){
            agent.setState(CallCenterAgent.STATE_ONLINE);
        }else if(!agent.getState().equals("busy") && !agent.getState().equals("away") && !agent.getState().equals("idle")
                && !agent.getState().startsWith("busy/") && !agent.getState().startsWith("away/") && !agent.getState().equals("online")){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("agent",agent)
            );
        }
        CallCenterAgent oldAgent = callCenterAgentDao.findByAppIdAndName(agent.getAppId(),agent.getName());
        if(oldAgent != null){
            Long lastRegTime = agentState.getLastRegTime(oldAgent.getId());
            //TODO 注册是否过期，过期执行注销过程
            if(lastRegTime == null || (System.currentTimeMillis() - lastRegTime) > AgentState.REG_EXPIRE){
                //TODO 注销
                logout(agent.getTenantId(), agent.getAppId(),agent.getSubaccountId(), agent.getName(), true);
            }else{
                //TODO 注册没有过期
                throw new AgentHasAlreadyLoggedInException();
            }
        }
        //分机ID
        String extensionId = agent.getExtension();
        if(StringUtils.isBlank(extensionId)){
           throw new RequestIllegalArgumentException(
                   new ExceptionContext().put("agent",agent)
           );
        }
        AppExtension extension = appExtensionService.findOne(agent.getAppId(),extensionId,agent.getSubaccountId());
        if(extension == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("agent",agent)
            );
        }
        //获取分机锁
        ExtensionLock extensionLock = new ExtensionLock(redisCacheService,extensionId);
        boolean lock = extensionLock.lock();
        //获取锁失败 抛异常
        if(!lock){
            throw new ExtensionBindingToAgentException(
                    new ExceptionContext().put("agent",agent)
            );
        }
        try{
            String extentionAgent = extensionState.getAgent(extensionId);
            if(StringUtils.isNotBlank(extentionAgent)){
                throw new ExtensionBindingToAgentException(
                        new ExceptionContext().put("agent",agent)
                                .put("extentionAgent",extentionAgent)
                );
            }
            String agentId = this.save(agent).getId();
            //匹配的条件集合
            List<Condition> suitedConditions = new ArrayList<>();
            Map<String,Long> conditionScore = new HashMap<>();
            Map<String,Integer> skillScore = new HashMap<>();
            if(agent.getSkills()!=null && agent.getSkills().size()>0){
                for(AgentSkill obj:agent.getSkills()){
                    //TODO 处理技能，不让技能名称重复
                    if(StringUtils.isBlank(obj.getName())){
                        throw new RequestIllegalArgumentException();
                    }
                    if(obj.getScore() == null){
                        obj.setScore(0);
                    }
                    if(obj.getEnabled() == null){
                        obj.setEnabled(false);
                    }
                    obj.setTenantId(agent.getTenantId());
                    obj.setAppId(agent.getAppId());
                    obj.setAgent(agentId);
                    agentSkillService.save(obj);
                    if(obj.getEnabled()){
                        skillScore.put(obj.getName(),obj.getScore());
                    }
                }

                //查询指定通道下所有条件集合，查出匹配的条件
            }
            setSuitedConditionsAndConditionScore(agent, suitedConditions, conditionScore, skillScore);
            //写入登录日志
            agentActionLogService.agentLogin(agent);
            //转成lua?
//            //TODO 设置座席分机
//            agentState.setExtension(agentId,extensionId);
//            agentState.setLastRegTime(agentId,System.currentTimeMillis());
//            agentState.setLastTime(agentId,System.currentTimeMillis());
//            agentState.setState(agentId,agent.getState());
//            //TODO 设置分机座席
//            extensionState.setAgent(extensionId,agentId);
//            suitedConditions.stream().forEach(condition -> {
//                //TODO 设置条件座席
//                cAs.add(condition.getId(),agentId,conditionScore.get(condition.getId()));
//                //TODO 设置座席条件
//                aCs.add(agentId,condition.getId(),condition.getPriority());
//            });
            //lua 实现
            int keyCount = 2;
            List<String> evalStr = new LinkedList<>();
            //座席状态key
            evalStr.add(AgentState.getKey(agentId));
            //分机状态key
            evalStr.add(ExtensionState.getKey(extensionId));
            //座席状态各个属性
            evalStr.add("extension," + extensionId + ",lastRegTime," + System.currentTimeMillis() + ",lastTime," + System.currentTimeMillis());
            //因为不知道传进来的state会不会包含","号，所以单独分出来
            evalStr.add(agent.getState());
            //分机的座席
            evalStr.add(agentId);
            if(suitedConditions != null && suitedConditions.size()>0){
                String aCsKey = ACs.getKey(agentId);
                StringBuffer bf = new StringBuffer();
                for(int i = 0;i<suitedConditions.size();i++){
                    keyCount ++;
                    Condition condition = suitedConditions.get(i);
                    //设置座席条件
                    bf.append((condition.getPriority()== null ? 0:condition.getPriority()) + "," + condition.getId() + ",");
                    //设置条件座席（注意以下两行代码顺序不能变）
                    evalStr.add(5 + i,conditionScore.get(condition.getId()) + "," + agentId);
                    evalStr.add(2,CAs.getKey(condition.getId()));
                }
                keyCount ++;
                //设置座席条件进参数列表（注意以下两行代码顺序不能变）
                evalStr.add(5 + suitedConditions.size(),bf.deleteCharAt(bf.length()-1).toString());
                evalStr.add(2,aCsKey);
            }
            redisCacheService.eval(Lua.AGENTLOGIN,keyCount ,evalStr.toArray(new String[0]));
            logger.info("execute lua params = {},{}",keyCount , JSONUtil2.objectToJson(evalStr));

            try{
                //TODO 异步
                //如果座席是空闲，触发座席找排队,此处与以上处理无关，所以不管成不成功，都返回
                if(agent.getState().contains(CallCenterAgent.STATE_IDLE)){
                    enQueueService.lookupQueue(agent.getTenantId(), agent.getAppId(),null, agentId);
                }
            }catch(Exception e){

            }
            return agentId;
        }finally{
            extensionLock.unlock();
        }
    }

    /**
     * 查询指定账号下所有条件集合，查出匹配的条件
     * @param agent 座席
     * @param suitedConditions 条件集合，请传入一个空的List对象
     * @param conditionScore 条件的分数，key为条件Id，请传入一个空的Map
     * @param skillScore 技能分数，key为技能名称，请传入一个空的Map
     */
    private void setSuitedConditionsAndConditionScore(CallCenterAgent agent, List<Condition> suitedConditions, Map<String, Long> conditionScore, Map<String, Integer> skillScore) throws YunhuniApiException {
        List<Condition> conditions = conditionService.getAll(agent.getTenantId(), agent.getAppId(), agent.getSubaccountId());
        conditions.stream().forEach(condition -> {
            if(ExpressionUtils.execWhereExpression(condition.getWhereExpression(),skillScore)){
                long score = ExpressionUtils.execSortExpression(condition.getSortExpression(), skillScore);
                conditionScore.put(condition.getId(),score);
                suitedConditions.add(condition);
            }
        });
    }

    //注销
    @Override
    public void logout(String tenantId, String appId,String subaccountId ,String agentName, boolean force) throws YunhuniApiException {
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("force",force)
            );
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId,agentName);
        if(agent == null ){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("force",force)
            );
        }
        AgentLock agentLock = new AgentLock(redisCacheService, agent.getId());
        boolean lock = agentLock.lock();
        if(!lock){
            //获取锁失败
            throw new AgentIsBusyException(
                    new ExceptionContext().put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("force",force)
                            .put("agentId",agent.getId())
            );
        }
        try{
            if(logger.isDebugEnabled()){
                logger.debug("开始注销座席：{}",agentName);
            }
            String agentId = agent.getId();
            String state = agentState.getState(agentId);

            if(StringUtils.isNotBlank(state) && (state.contains(CallCenterAgent.STATE_FETCHING)||state.contains(CallCenterAgent.STATE_TALKING))){
                if(force){
                    //TODO
                    if(logger.isDebugEnabled()){
                        logger.debug("强行注销座席：{}",agentName);
                    }
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("座席正忙：{}",agentName);
                    }
                    // 座席正忙
                    throw new AgentIsBusyException(
                            new ExceptionContext().put("tenantId",tenantId)
                                    .put("appId",appId)
                                    .put("subaccountId",subaccountId)
                                    .put("agentName",agentName)
                                    .put("force",force)
                                    .put("agentId",agent.getId())
                    );
                }
            }
            agentSkillService.deleteByAgent(agentId);
            try {
                this.delete(agent);
                //写入注销日志
                agentActionLogService.agentLogout(agent);
            } catch (Exception e) {
                logger.error("删除座席出错", JSONUtil.objectToJson(agent));
                logger.error("删除座席出错",e);
            }
            String extension = agentState.getExtension(agentId);
            //TODO LUA?
            if(StringUtils.isNotBlank(extension)){
                extensionState.deleteAgent(extension);
            }
            agentState.delete(agentId);
            Set<String> conditionIds = aCs.getAll(agentId);
            if(conditionIds != null){
                conditionIds.stream().forEach(conditionId -> {
                    cAs.remove(conditionId,agentId);
                });
            }
            aCs.delete(agentId);
        }finally {
            agentLock.unlock();
        }

    }

    @Override
    public void keepAlive(String appId,String subaccountId, String agentName) throws YunhuniApiException{
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }

        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }
        agentState.setLastRegTime(agent.getId(),System.currentTimeMillis());
    }

    @Override
    public List<String> getAgentIdsBySubaccountId(String tenantId,String appId,String subaccountId){
        String sql = "select id  from db_lsxy_bi_yunhuni.tb_bi_call_center_agent " +
                "where tenant_id=\""+tenantId+"\" and app_id=\""+appId+"\" and subaccount_id=\""+subaccountId+"\" and deleted = 0";
        return jdbcTemplate.queryForList(sql, new Object[]{}, String.class);
    }

    @Override
    public CallCenterAgent get(String appId, String agentName) throws YunhuniApiException{
        return this.get(appId,null, agentName);
    }

    @Override
    public CallCenterAgent get(String appId,String subaccountId, String agentName) throws YunhuniApiException {
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("subaccountId",subaccountId)
            );
        }
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("subaccountId",subaccountId)
            );
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId, agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("subaccountId",subaccountId)
            );
        }
        AgentState.Model model = agentState.get(agent.getId());
        //状态
        agent.setState(model.getState());
        //分机
        agent.setExtension(model.getExtension());
        List<AgentSkill> skills = agentSkillService.findAllByAgent(agent.getId());
        //技能
        agent.setSkills(skills);
        return agent;
    }

    @Override
    public String getId(String appId, String agentName) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId, agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
            );
        }
        return agent.getId();
    }

    @Override
    public String getId(String appId,String subaccountId, String agentName) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId, subaccountId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }
        return agent.getId();
    }

    @Override
    public Page getPageForPotal(String appId, Integer pageNo, Integer pageSize,String agentNum,String subaccountId) throws YunhuniApiException{
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
            );
        }
        List<String> agentIds = new ArrayList<>();
        Page page;
        String hql = "from CallCenterAgent obj where obj.appId=?1";
        if(StringUtils.isNotEmpty(agentNum)){
            hql += " and obj.name like '%"+agentNum+"%'";
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            hql += " and obj.subaccountId in ("+ subaccountId+") ";
        }
        page = this.pageList(hql, pageNo, pageSize, appId);

        List<CallCenterAgent> result = page.getResult();
        //将所有座席ID放入集合中
        result.stream().forEach(agent ->agentIds.add(agent.getId()));
        //查询这些座席的技能
        List<AgentSkill> skills = agentSkillService.findAllByAgents(agentIds);
        //分组这些技能，以座席Id为key放入Map中
        Map<String, List<AgentSkill>> collect = skills.stream().collect(Collectors.groupingBy(AgentSkill::getAgent, Collectors.toList()));
        List<String> extensionIds = new ArrayList<>();
        result.stream().forEach(agent -> {
            AgentState.Model model = agentState.get(agent.getId());
            agent.setState(model.getState());
            agent.setExtension(model.getExtension());
            if(null != model.getExtension()) {
                extensionIds.add(model.getExtension());
            }
            agent.setSkills(collect.get(agent.getId()));
        });
        Map<String,AppExtension> map = new HashMap<>();
        Iterable<AppExtension> appExtensions = appExtensionService.findAll(extensionIds);
        if (appExtensions != null) {
            for (AppExtension e : appExtensions) {
                map.put(e.getId(), e);
            }
        }
        result.stream().forEach(agent ->{
            AppExtension extension = map.get(agent.getExtension());
            if(extension != null){
                agent.setExtension(extension.getUser());
            }else{
                agent.setExtension(null);
            }
        });

        return page;
    }

    @Override
    public Page getPageForApiGW(String appId, String subaccountId, Integer pageNo, Integer pageSize) throws YunhuniApiException {
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                    .put("subaccountId",subaccountId)
            );
        }
        List<String> agentIds = new ArrayList<>();
        Page page;
        if(StringUtils.isNotBlank(subaccountId)){
            String hql = "from CallCenterAgent obj where obj.appId=?1 and obj.subaccountId=?2";
            page = this.pageList(hql, pageNo, pageSize, appId,subaccountId);
        }else{
            String hql = "from CallCenterAgent obj where obj.appId=?1 and obj.subaccountId is null";
            page = this.pageList(hql, pageNo, pageSize, appId);
        }

        List<CallCenterAgent> result = page.getResult();
        //将所有座席ID放入集合中
        result.stream().forEach(agent ->agentIds.add(agent.getId()));
        //查询这些座席的技能
        List<AgentSkill> skills = agentSkillService.findAllByAgents(agentIds);
        //分组这些技能，以座席Id为key放入Map中
        Map<String, List<AgentSkill>> collect = skills.stream().collect(Collectors.groupingBy(AgentSkill::getAgent, Collectors.toList()));
        result.stream().forEach(agent -> {
            AgentState.Model model = agentState.get(agent.getId());
            agent.setState(model.getState());
            agent.setExtension(model.getExtension());
            agent.setSkills(collect.get(agent.getId()));
        });
        return page;
    }

    @Override
    public void extension(String appId, String agentName,String extensionId,String subaccountId) throws YunhuniApiException{
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        if(StringUtils.isBlank(extensionId)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        AppExtension extension = appExtensionService.findOne(agent.getAppId(),extensionId,subaccountId);
        if(extension == null){
            throw new ExtensionNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        //获取分机锁
        ExtensionLock extensionLock = new ExtensionLock(redisCacheService,extensionId);
        boolean lock = extensionLock.lock();
        //获取锁失败 抛异常
        if(!lock){
            throw new ExtensionBindingToAgentException(
                    new ExceptionContext().put("appId",appId)
                            .put("agentName",agentName)
                            .put("extensionId",extensionId)
                            .put("subaccountId",subaccountId)
            );
        }
        try{
            String extentionAgent = extensionState.getAgent(extensionId);
            if(StringUtils.isNotBlank(extentionAgent)){
                if(extentionAgent.equals(agent.getId())){
                    return;
                }else{
                    throw new ExtensionBindingToAgentException(
                            new ExceptionContext().put("appId",appId)
                                    .put("agentName",agentName)
                                    .put("extensionId",extensionId)
                                    .put("subaccountId",subaccountId)
                    );
                }
            }
            String oldExtension = agentState.getExtension(agent.getId());
            if(StringUtils.isNotBlank(oldExtension)){
                extensionState.deleteAgent(oldExtension);
            }
            extensionState.setAgent(extensionId,agent.getId());
            agentState.setExtension(agent.getId(),extensionId);
        }finally {
            extensionLock.unlock();
        }

    }

    @Override
    public String state(String appId,String subaccountId, String agentName, String state) throws YunhuniApiException{
        if(StringUtils.isBlank(appId)){
            throw new RequestIllegalArgumentException(
                            new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("state",state)
            );
        }
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("state",state)
            );
        }
        if(StringUtils.isBlank(state)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("state",state)
            );
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
                            .put("state",state)
            );
        }
        return this.state(agent.getTenantId(),agent.getAppId(),agent.getId(),state,false);
    }

    @Override
    public String state(String tenantId,String appId,String agentId, String state,boolean force) throws YunhuniApiException{
        if(logger.isDebugEnabled()){
            logger.info("[{}][{}]agent={},state={}",tenantId,appId,agentId,state);
        }
        AgentLock agentLock = new AgentLock(redisCacheService, agentId);
        boolean lock = agentLock.lock();
        if(!lock){
            //获取锁失败
            throw new SystemBusyException(
                    new ExceptionContext()
                            .put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("agentId",agentId)
                            .put("state",state)
                            .put("force",force)
            );
        }
        try{
            if(!force){
                String curState = agentState.getState(agentId);
                if(StringUtils.isNotBlank(curState) && (curState.contains(CallCenterAgent.STATE_FETCHING)||curState.contains(CallCenterAgent.STATE_TALKING))){
                    // 座席正忙
                    throw new AgentIsBusyException(
                            new ExceptionContext()
                                    .put("tenantId",tenantId)
                                    .put("appId",appId)
                                    .put("agentId",agentId)
                                    .put("state",state)
                                    .put("force",force)
                                    .put("curState",force)
                    );
                }
            }
            if(state == null){
                state = CallCenterAgent.STATE_IDLE;
            }
            agentState.setState(agentId,state);
            if(state.contains(CallCenterAgent.STATE_IDLE)){
                try{
                    enQueueService.lookupQueue(tenantId,appId,null,agentId);
                }catch (Throwable t){
                    logger.info("座席空闲，查找排队出错:{}",t);
                }
            }
            return state;
        }finally {
            agentLock.unlock();
        }
    }

    @Override
    public String getState(String agentId){
        String state = agentState.getState(agentId);
        return state;
    }
    @Override
    public void skills(String tenantId, String appId, String subaccountId , String agentName, List<AgentSkillOperationDTO> skillOpts) throws YunhuniApiException{
        if(StringUtils.isBlank(agentName)){
            throw new RequestIllegalArgumentException(
                    new ExceptionContext().put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndSubaccountIdAndName(appId,subaccountId,agentName);
        if(agent == null){
            //座席不存在
            throw new AgentNotExistException(
                    new ExceptionContext().put("tenantId",tenantId)
                            .put("appId",appId)
                            .put("subaccountId",subaccountId)
                            .put("agentName",agentName)
            );
        }
        String agentId = agent.getId();

        if(skillOpts != null && skillOpts.size() > 0){
            //匹配的条件集合
            List<Condition> suitedConditions = new ArrayList<>();
            Map<String,Long> conditionScore = new HashMap<>();

            //旧的技能
            List<AgentSkill> skills = agentSkillService.findAllByAgent(agentId);
            //将技能放到map,key是技能名
            Map<String,AgentSkill> skillMap = new HashMap<>();
            skills.stream().forEach(skill -> skillMap.put(skill.getName(),skill));
            int count = 0;
            for(AgentSkillOperationDTO opt :skillOpts){
                switch (opt.getOpt()){
                    case 0:{
                        //删除全部
                        agentSkillService.deleteByAgent(agentId);
                        skillMap.clear();
                        count ++;
                        break;
                    }
                    case 1:{
                        //修改
                        if(StringUtils.isNotBlank(opt.getName())){
                            AgentSkill agentSkill = skillMap.get(opt.getName());
                            if(agentSkill != null){
                                if(opt.getScore() != null){
                                    agentSkill.setScore(opt.getScore());
                                }
                                if(opt.getEnabled() != null){
                                    agentSkill.setEnabled(opt.getEnabled());
                                }
                                agentSkillService.save(agentSkill);
                                count ++;
                            }else{
                                AgentSkill newSkill = new AgentSkill(tenantId,appId,agentId,opt.getName(),
                                        opt.getScore() == null?0:opt.getScore(),
                                        opt.getEnabled()== null?false:opt.getEnabled());
                                agentSkillService.save(newSkill);
                                skillMap.put(newSkill.getName(),newSkill);
                                count ++;
                            }
                        }
                        break;
                    }
                    case 2:{
                        //删除一个
                        if(StringUtils.isNotBlank(opt.getName())){
                            agentSkillService.deleteByAgentAndName(agentId,opt.getName());
                            skillMap.remove(opt.getName());
                            count ++;
                        }
                        break;
                    }
                }
            }
            if(count == 0 ){
                throw new RequestIllegalArgumentException();
            }

            Collection<AgentSkill> newSkills = skillMap.values();
            Map<String,Integer> skillScore = new HashMap<>();
            newSkills.stream().forEach(skill -> {
                if(skill.getEnabled()){
                    skillScore.put(skill.getName(),skill.getScore());
                }
            });
            //查询指定通道下所有条件集合，查出匹配的条件
            setSuitedConditionsAndConditionScore(agent, suitedConditions, conditionScore, skillScore);

            Set<String> oldConditionIds = aCs.getAll(agentId);

            suitedConditions.stream().forEach(condition -> {
                oldConditionIds.remove(condition.getId());
                //TODO 设置条件座席
                cAs.add(condition.getId(),agentId,conditionScore.get(condition.getId()));
                //TODO 设置座席条件
                aCs.add(agentId,condition.getId(),condition.getPriority());
            });
            oldConditionIds.stream().forEach(cId -> {
                cAs.remove(cId,agentId);
                aCs.remove(agentId,cId);
            });
        }
    }

}
