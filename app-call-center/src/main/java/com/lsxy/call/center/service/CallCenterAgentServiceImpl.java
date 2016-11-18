package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.dao.AgentSkillDao;
import com.lsxy.call.center.dao.CallCenterAgentDao;
import com.lsxy.call.center.states.lock.ExtensionLock;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.utils.ExpressionUtils;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private AgentSkillDao agentSkillDao;
    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    ChannelService channelService;
    @Autowired
    ConditionService conditionService;
    @Autowired
    ExtensionState extensionState;
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    private ACs aCs;
    @Autowired
    private CAs cAs;

    @Override
    public BaseDaoInterface<CallCenterAgent, Serializable> getDao() {
        return callCenterAgentDao;
    }

    //登陆
    @Override
    public String login(CallCenterAgent agent) throws YunhuniApiException {
        Channel channel;
        try{
            channel = channelService.findOne(agent.getTenantId(), agent.getAppId(), agent.getChannel());
        }catch (IllegalArgumentException e){
            throw new RequestIllegalArgumentException();
        }
        CallCenterAgent oldAgent = callCenterAgentDao.findByAppIdAndChannelAndName(agent.getAppId(),agent.getChannel(),agent.getName());
        if(oldAgent != null){
            //TODO 注册是否过期，过期执行注销过程

            //TODO 注册没有过期
            throw new AgentHasAlreadyLoggedInException();
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
        try{
            //获取锁失败 抛异常
            if(!lock){
                throw new ExtensionBindingToAgentException();
            }
            String extentionAgent = extensionState.getAgent(extensionId);
            if(StringUtils.isNotBlank(extentionAgent)){
                throw new ExtensionBindingToAgentException();
            }
            String agentId = this.save(agent).getId();
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
                List<Condition> suitedConditions = new ArrayList<>();
                for(Condition condition:conditions){
                    if(ExpressionUtils.execWhereExpression(condition.getWhereExpression(),vars)){
                        suitedConditions.add(condition);
                    }
                }

            }

            extensionState.setAgent(extensionId,agentId);
            return agentId;
        }finally{
            extensionLock.unlock();
        }
    }

    //注销
    @Override
    public boolean logout(String agentId){
        agentSkillDao.deleteByAgent(agentId);
        //TODO 删除坐席的分机列表
        try {
            this.delete(agentId);
        } catch (Throwable e) {
            throw new IllegalArgumentException(e);
        }
        return true;
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
        agentSkillDao.updateActiveByAgent(enable,agent,skillName);
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

}
