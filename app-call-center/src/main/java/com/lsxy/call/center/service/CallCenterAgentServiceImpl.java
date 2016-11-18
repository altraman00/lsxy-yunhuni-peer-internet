package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.operations.AgentSkillOperationDTO;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.dao.CallCenterAgentDao;
import com.lsxy.call.center.states.lock.AgentLock;
import com.lsxy.call.center.states.lock.ExtensionLock;
import com.lsxy.call.center.states.state.AgentState;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.base.BeanSelfAware;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.JSONUtil;
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
public class CallCenterAgentServiceImpl extends AbstractService<CallCenterAgent> implements CallCenterAgentService,BeanSelfAware {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterAgentServiceImpl.class);

    private CallCenterAgentService self;
    @Override
    public void setSelf(Object proxyBean) {
        //设置此对象的代理对象
        this.self = (CallCenterAgentService) proxyBean;
    }
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
    private ChannelService channelService;
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
        try{
            channelService.findOne(agent.getTenantId(), agent.getAppId(), agent.getChannel());
        }catch (IllegalArgumentException e){
            throw new RequestIllegalArgumentException();
        }
        CallCenterAgent oldAgent = callCenterAgentDao.findByAppIdAndName(agent.getAppId(),agent.getName());
        if(oldAgent != null){
            Long lastRegTime = agentState.getLastRegTime(oldAgent.getId());
            //TODO 注册是否过期，过期执行注销过程
            if(lastRegTime == null || (System.currentTimeMillis() - lastRegTime) > 5 * 60 * 1000){
                //TODO 注销
                logout(agent.getTenantId(), agent.getAppId(), agent.getName(), false);
            }else{
                //TODO 注册没有过期
                throw new AgentHasAlreadyLoggedInException();
            }
        }
        //分机ID
        String extensionId = agent.getExtension();
        if(StringUtils.isBlank(extensionId)){
           throw new RequestIllegalArgumentException();
        }
        AppExtension extension = appExtensionService.findOne(agent.getAppId(),extensionId);
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        //获取分机锁
        ExtensionLock extensionLock = new ExtensionLock(redisCacheService,extensionId);
        boolean lock = extensionLock.lock();
        //获取锁失败 抛异常
        if(!lock){
            throw new ExtensionBindingToAgentException();
        }
        try{
            String extentionAgent = extensionState.getAgent(extensionId);
            if(StringUtils.isNotBlank(extentionAgent)){
                throw new ExtensionBindingToAgentException();
            }
            String agentId = this.save(agent).getId();
            //匹配的条件集合
            List<Condition> suitedConditions = new ArrayList<>();
            Map<String,Long> conditionScore = new HashMap<>();
            if(agent.getSkills()!=null && agent.getSkills().size()>0){
                Map<String,Integer> skillScore = new HashMap<>();
                for(AgentSkill obj:agent.getSkills()){
                    //TODO 处理技能，不让技能名称重复
                    if(StringUtils.isBlank(obj.getName())){
                        throw new RequestIllegalArgumentException();
                    }
                    if(obj.getScore() == null){
                        throw new RequestIllegalArgumentException();
                    }
                    if(obj.getEnabled() == null){
                        throw new RequestIllegalArgumentException();
                    }
                    obj.setTenantId(agent.getTenantId());
                    obj.setAppId(agent.getAppId());
                    obj.setAgent(agentId);
                    agentSkillService.save(obj);
                    skillScore.put(obj.getName(),obj.getScore());
                }

                //查询指定通道下所有条件集合，查出匹配的条件
                setSuitedConditionsAndConditionScore(agent, suitedConditions, conditionScore, skillScore);
            }
            //写入登录日志
            agentActionLogService.agentLogin(agent);
            //转成lua?
            //TODO 设置座席分机
            agentState.setExtension(agentId,extensionId);
            agentState.setState(agentId,agent.getState());
            agentState.setLastRegTime(agentId,System.currentTimeMillis());
            agentState.setLastTime(agentId,System.currentTimeMillis());
            //TODO 设置分机座席
            extensionState.setAgent(extensionId,agentId);
            suitedConditions.parallelStream().forEach(condition -> {
                //TODO 设置条件座席
                cAs.add(condition.getId(),agentId,conditionScore.get(condition.getId()));
                //TODO 设置座席条件
                aCs.add(agentId,condition.getId(),condition.getPriority());
            });
            //TODO 如果座席是空闲，触发座席找排队
            if(agent.getState().contains("idle")){

            }
            return agentId;
        }finally{
            extensionLock.unlock();
        }
    }

    /**
     * 查询指定通道下所有条件集合，查出匹配的条件
     * @param agent 座席
     * @param suitedConditions 条件集合，请传入一个空的List对象
     * @param conditionScore 条件的分数，key为条件Id，请传入一个空的Map
     * @param skillScore 技能分数，key为技能名称，请传入一个空的Map
     */
    private void setSuitedConditionsAndConditionScore(CallCenterAgent agent, List<Condition> suitedConditions, Map<String, Long> conditionScore, Map<String, Integer> skillScore) {
        List<Condition> conditions = conditionService.getAll(agent.getTenantId(), agent.getAppId(), agent.getChannel());
        conditions.parallelStream().forEach(condition -> {
            if(ExpressionUtils.execWhereExpression(condition.getWhereExpression(),skillScore)){
                long score = ExpressionUtils.execSortExpression(condition.getSortExpression(), skillScore);
                conditionScore.put(condition.getId(),score);
                suitedConditions.add(condition);
            }
        });
    }

    //注销
    @Override
    public void logout(String tenantId, String appId, String agentName, boolean force) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException();
        }
        AgentLock agentLock = new AgentLock(redisCacheService, agent.getId());
        boolean lock = agentLock.lock();
        if(!lock){
            //获取锁失败
            throw new ExtensionBindingToAgentException();
        }
        try{
            String agentId = agent.getId();
            String state = agentState.getState(agentId);

            if(StringUtils.isNotBlank(state) && (state.contains(AgentState.Model.STATE_FETCHING)||state.contains(AgentState.Model.STATE_TALKING))){
                if(force){
                    //TODO 切断平台和座席之间的呼叫
                }else{
                    // 座席正忙
                    throw new AgentIsBusyException();
                }
            }
            agentSkillService.deleteByAgent(agentId);
            try {
                this.delete(agentId);
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
                conditionIds.parallelStream().forEach(conditionId -> {
                    cAs.remove(conditionId,agentId);
                });
            }
            aCs.delete(agentId);
        }finally {
            agentLock.unlock();
        }

    }

    @Override
    public void keepAlive(String appId, String agentName) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException();
        }
        agentState.setLastRegTime(agent.getId(),System.currentTimeMillis());
    }

    @Override
    public List<String> getAgentIdsByChannel(String tenantId,String appId,String channelId){
        String sql = "select id  from db_lsxy_bi_yunhuni.tb_bi_call_center_agent " +
                "where tenant_id=\""+tenantId+"\" and app_id=\""+appId+"\" and channel=\""+channelId+"\" and deleted = 0";

        return jdbcTemplate.queryForList(sql, new Object[]{}, String.class);
    }

    @Override
    public CallCenterAgent get(String appId, String agentName) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId, agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException();
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
    public Page getPage(String appId, Integer pageNo, Integer pageSize) throws YunhuniApiException{
        List<String> agentIds = new ArrayList<>();
        String hql = "from CallCenterAgent obj where obj.appId=?1";
        Page page = this.pageList(hql, pageNo, pageSize, appId);
        List<CallCenterAgent> result = page.getResult();
        //将所有座席ID放入集合中
        result.parallelStream().forEach(agent ->agentIds.add(agent.getId()));
        //查询这些座席的技能
        List<AgentSkill> skills = agentSkillService.findAllByAgents(agentIds);
        //分组这些技能，以座席Id为key放入Map中
        Map<String, List<AgentSkill>> collect = skills.parallelStream().collect(Collectors.groupingBy(AgentSkill::getAgent, Collectors.toList()));
        result.parallelStream().forEach(agent -> {
            AgentState.Model model = agentState.get(agent.getId());
            agent.setState(model.getState());
            agent.setExtension(model.getExtension());
            agent.setSkills(collect.get(agent.getId()));
        });
        return page;
    }

    @Override
    public void extension(String appId, String agentName,String extensionId) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException();
        }
        AppExtension extension = appExtensionService.findOne(agent.getAppId(),extensionId);
        if(extension == null){
            throw new ExtensionNotExistException();
        }
        //获取分机锁
        ExtensionLock extensionLock = new ExtensionLock(redisCacheService,extensionId);
        boolean lock = extensionLock.lock();
        //获取锁失败 抛异常
        if(!lock){
            throw new ExtensionBindingToAgentException();
        }
        try{
            String extentionAgent = extensionState.getAgent(extensionId);
            if(StringUtils.isNotBlank(extentionAgent)){
                if(extentionAgent.equals(agent.getId())){
                    return;
                }else{
                    throw new ExtensionBindingToAgentException();
                }
            }
            extensionState.setAgent(extensionId,agent.getId());
            agentState.setExtension(agent.getId(),extensionId);
        }finally {
            extensionLock.unlock();
        }

    }

    @Override
    public void state(String appId, String agentName, String state) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId,agentName);
        if(agent == null){
            // 座席不存在
            throw new AgentNotExistException();
        }

        AgentLock agentLock = new AgentLock(redisCacheService, agent.getId());
        boolean lock = agentLock.lock();
        if(!lock){
            //获取锁失败
            throw new ExtensionBindingToAgentException();
        }
        try{
            String curState = agentState.getState(agent.getId());
            if(curState.contains(AgentState.Model.STATE_FETCHING)||curState.contains(AgentState.Model.STATE_TALKING)){
                // 座席正忙
                throw new AgentIsBusyException();
            }
            agentState.setState(agent.getId(),state);
            if(state.contains(AgentState.Model.STATE_IDLE)){
                //TODO 找排队事件
            }
        }finally {
            agentLock.unlock();
        }

    }

    @Override
    public void skills(String tenantId, String appId, String agentName, List<AgentSkillOperationDTO> skillOpts) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndName(appId,agentName);
        if(agent == null){
            //座席不存在
            throw new AgentNotExistException();
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
            skills.parallelStream().forEach(skill -> skillMap.put(skill.getName(),skill));
            skillOpts.stream().forEach(opt -> {
                switch (opt.getOpt()){
                    case 0:{
                        //删除全部
                        agentSkillService.deleteByAgent(agentId);
                        skillMap.clear();
                        break;
                    }
                    case 1:{
                        //修改
                        if(StringUtils.isNotBlank(opt.getName())){
                            AgentSkill agentSkill = skillMap.get(opt.getName());
                            if(agentSkill != null){
                                agentSkill.setScore(opt.getScore());
                                agentSkill.setEnabled(opt.getEnabled());
                                agentSkillService.save(agentSkill);
                            }else{
                                AgentSkill newSkill = new AgentSkill(tenantId,appId,agentId,opt.getName(),opt.getScore(),opt.getEnabled());
                                agentSkillService.save(newSkill);
                                skillMap.put(newSkill.getName(),newSkill);
                            }
                        }
                        break;
                    }
                    case 2:{
                        //删除一个
                        agentSkillService.deleteByAgentAndName(agentId,opt.getName());
                        skillMap.remove(opt.getName());
                        break;
                    }
                }
            });
            Collection<AgentSkill> newSkills = skillMap.values();
            Map<String,Integer> skillScore = new HashMap<>();
            newSkills.parallelStream().forEach(skill -> skillScore.put(skill.getName(),skill.getScore()));
            //查询指定通道下所有条件集合，查出匹配的条件
            setSuitedConditionsAndConditionScore(agent, suitedConditions, conditionScore, skillScore);

            Set<String> oldConditionIds = aCs.getAll(agentId);
            Set<String> removeConditionIds = new HashSet<>();

            suitedConditions.parallelStream().forEach(condition -> {
                oldConditionIds.remove(condition.getId());
                //TODO 设置条件座席
                cAs.add(condition.getId(),agentId,conditionScore.get(condition.getId()));
                //TODO 设置座席条件
                aCs.add(agentId,condition.getId(),condition.getPriority());
            });
            removeConditionIds.parallelStream().forEach(cId -> {
                cAs.remove(cId,agentId);
                aCs.remove(agentId,cId);
            });
        }
    }


}
