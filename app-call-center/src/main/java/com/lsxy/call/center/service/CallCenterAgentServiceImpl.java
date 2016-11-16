package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AgentSkill;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.model.Condition;
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
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.FileUtil;
import com.lsxy.framework.core.utils.Page;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collector;
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

    //登陆
    @Override
    public String login(CallCenterAgent agent) throws YunhuniApiException {
        try{
            channelService.findOne(agent.getTenantId(), agent.getAppId(), agent.getChannel());
        }catch (IllegalArgumentException e){
            throw new RequestIllegalArgumentException();
        }
        CallCenterAgent oldAgent = callCenterAgentDao.findByAppIdAndChannelAndName(agent.getAppId(),agent.getChannel(),agent.getName());
        if(oldAgent != null){
            Long lastRegTime = agentState.getLastRegTime(oldAgent.getId());
            //TODO 注册是否过期，过期执行注销过程
            if(lastRegTime == null || (System.currentTimeMillis() - lastRegTime) > 5 * 60 * 1000){
                //TODO 注销
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
                Map<String,Integer> vars = new HashMap<>();
                for (AgentSkill obj : agent.getSkills()) {
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
                    vars.put(obj.getName(),obj.getScore());
                }
                //查询指定通道下所有条件集合，查出匹配的条件
                List<Condition> conditions = conditionService.getAll(agent.getTenantId(), agent.getAppId(), agent.getChannel());
                for(Condition condition:conditions){
                    if(ExpressionUtils.execWhereExpression(condition.getWhereExpression(),vars)){
                        long score = ExpressionUtils.execSortExpression(condition.getSortExpression(), vars);
                        conditionScore.put(condition.getId(),score);
                        suitedConditions.add(condition);
                    }
                }
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
            for(Condition condition:suitedConditions){
                //TODO 设置条件座席
                cAs.add(condition.getId(),agentId,conditionScore.get(condition.getId()));
                //TODO 设置座席条件
                aCs.add(agentId,condition.getId(),condition.getPriority());
            }
            //TODO 如果座席是空闲，触发座席找排队
            if(agent.getState().contains("idle")){

            }
            return agentId;
        }finally{
            extensionLock.unlock();
        }
    }

    //注销
    @Override
    public void logout(String tenantId, String appId, String channel, String agentName, boolean force) throws YunhuniApiException {
        try{
            channelService.findOne(tenantId, appId, channel);
        }catch (IllegalArgumentException e){
            throw new RequestIllegalArgumentException();
        }
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndChannelAndName(appId,channel,agentName);
        if(agent == null){
            //TODO 座席不存在
            throw new RequestIllegalArgumentException();
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

            if(StringUtils.isNotBlank(state) && (state.contains("fetching")||state.contains("talking"))){
                if(force){
                    //TODO 切断平台和座席之间的呼叫
                }else{
                    //TODO 座席正忙
                    throw new RuntimeException("座席正忙");
                }
            }
            agentSkillService.deleteByAgent(agentId);
            try {
                this.delete(agentId);
                //写入注销日志
                agentActionLogService.agentLogout(agent);
            } catch (Exception e) {
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
                for(String conditionId:conditionIds){
                    cAs.remove(conditionId,agentId);
                }
            }
            aCs.delete(agentId);
        }finally {
            agentLock.unlock();
        }

    }

    @Override
    public void keepAlive(String appId, String channel, String agentName) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndChannelAndName(appId,channel,agentName);
        if(agent == null){
            //TODO 座席不存在
            throw new RequestIllegalArgumentException();
        }
        agentState.setLastRegTime(agent.getId(),System.currentTimeMillis());
    }

    //设置坐席状态
    public boolean setState(String id,String state){
        CallCenterAgent agent = this.findById(id);
        if(agent == null){
            return true;
        }
        //TODO 设置坐席的状态
        this.save(agent);
        return true;
    }
    //技能迁入迁出
    @Override
    public boolean checkInSkill(String agent,String skillName,Boolean enable){
//        agentSkillDao.updateActiveByAgent(enable,agent,skillName);
        return true;
    }
    //技能追加
    @Override
    public boolean appendSkill(String tenantId,String appId,String agent,String name,Integer level,Boolean active){
        AgentSkill skill = new AgentSkill();
        skill.setTenantId(tenantId);
        skill.setAppId(appId);
        skill.setAgent(agent);
        skill.setName(name);
        skill.setScore(level);
        skill.setEnabled(active);
        agentSkillService.save(skill);
        return true;
    }

    @Override
    public List<String> getAgentIdsByChannel(String tenantId,String appId,String channelId){
        String sql = "select id  from db_lsxy_bi_yunhuni.tb_bi_call_center_agent " +
                "where tenant_id=\""+tenantId+"\" and app_id=\""+appId+"\" and channel=\""+channelId+"\" and deleted = 0";

        return jdbcTemplate.queryForList(sql, new Object[]{}, String.class);
    }

    @Override
    public CallCenterAgent get(String appId, String channel, String agentName) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndChannelAndName(appId,channel, agentName);
        if(agent == null){
            //TODO 座席不存在
            throw new RequestIllegalArgumentException();
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
    public Page getPage(String appId, String channel, Integer pageNo, Integer pageSize) throws YunhuniApiException{
        List<String> agentIds = new ArrayList<>();
        String hql = "from CallCenterAgent obj where obj.appId=?1 and obj.channel=?2";
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
    public void extension(String appId, String channel, String agentName,String extensionId) throws YunhuniApiException{
        CallCenterAgent agent = callCenterAgentDao.findByAppIdAndChannelAndName(appId,channel,agentName);
        if(agent == null){
            //TODO 座席不存在
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

}
