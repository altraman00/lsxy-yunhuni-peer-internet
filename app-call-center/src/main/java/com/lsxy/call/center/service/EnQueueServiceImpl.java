package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.states.lock.AgentLock;
import com.lsxy.call.center.states.lock.QueueLock;
import com.lsxy.call.center.states.state.AgentState;
import com.lsxy.call.center.states.state.ExtensionState;
import com.lsxy.call.center.states.statics.ACs;
import com.lsxy.call.center.states.statics.CAs;
import com.lsxy.call.center.states.statics.CQs;
import com.lsxy.call.center.utils.CQSBatchInserter;
import com.lsxy.call.center.utils.Lua;
import com.lsxy.call.center.utils.QueueBatchInserter;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.ChannelNotExistException;
import com.lsxy.framework.core.exceptions.api.ConditionNotExistException;
import com.lsxy.framework.core.utils.*;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.EnqueueTimeoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by liuws on 2016/11/14.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class EnQueueServiceImpl implements EnQueueService{

    private static final Logger logger = LoggerFactory.getLogger(EnQueueServiceImpl.class);

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ConditionService conditionService;

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    @Autowired
    private AppExtensionService appExtensionService;

    @Autowired
    private CallCenterAgentService callCenterAgentService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Reference(lazy = true,check = false,timeout = 30000)
    private DeQueueService deQueueService;

    @Autowired
    private AgentState agentState;

    @Autowired
    private CQs cQs;

    @Autowired
    private CQSBatchInserter cqsBatchInserter;

    @Autowired
    private MQService mqService;

    @Autowired
    private QueueBatchInserter queueBatchInserter;

    private CallCenterQueue save(String num,Condition condition,String callId,BaseEnQueue baseEnQueue){
        CallCenterQueue queue = new CallCenterQueue();
        queue.setId(UUIDGenerator.uuid());
        queue.setTenantId(condition.getTenantId());
        queue.setAppId(condition.getAppId());
        queue.setRelevanceId("");
        queue.setCondition(condition.getId());
        queue.setStartTime(new Date());
        queue.setNum(num);
        queue.setOriginCallId(callId);
        queue.setEnqueue(JSONUtil.objectToJson(baseEnQueue));
        queueBatchInserter.put(queue);
        return queue;
    }

    private void addCQS(String conditionId,String queueId){
        cqsBatchInserter.put(conditionId, queueId);
    }

    private void publishTimeoutEvent(Condition condition,String queueId,String callId){
        long start = 0;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        mqService.publish(new EnqueueTimeoutEvent(condition.getId(),
                queueId,
                condition.getTenantId(),
                condition.getAppId(),
                callId,
                (condition.getQueueTimeout() == null ? 0 : condition.getQueueTimeout()) * 1000));
        if(logger.isDebugEnabled()){
            logger.debug("发布排队超时事件，耗时={}",(System.currentTimeMillis() - start));
        }
    }

    /**
     * 排队,通过 dubbo返回结果给  区域管理器
     * @param tenantId
     * @param appId
     * @param callId 参与排队的callId
     * @param enQueue
     */
    @Override
    public void lookupAgent(String tenantId, String appId,String num, String callId, EnQueue enQueue){
        String queueId = null;
        try{
            if(tenantId == null){
                throw new IllegalArgumentException("tenantId 不能为null");
            }
            if(appId == null){
                throw new IllegalArgumentException("appId 不能为null");
            }
            if(callId == null){
                throw new IllegalArgumentException("callId 不能为null");
            }
            if(enQueue == null){
                throw new IllegalArgumentException("enQueue 不能为null");
            }
            Channel channel = channelService.findById(enQueue.getChannel());
            if(channel == null){
                throw new ChannelNotExistException();
            }
            if(!tenantId.equals(channel.getTenantId())){
                throw new ChannelNotExistException();
            }
            if(!appId.equals(channel.getAppId())){
                throw new ChannelNotExistException();
            }

            String conditionId = enQueue.getRoute().getCondition().getId();
            Condition condition = conditionService.findById(conditionId);
            if(condition == null){
                throw new ConditionNotExistException();
            }
            if(!tenantId.equals(condition.getTenantId())){
                throw new ConditionNotExistException();
            }
            if(!appId.equals(condition.getAppId())){
                throw new ConditionNotExistException();
            }
            if(!condition.getChannelId().equals(channel.getId())){
                throw new ConditionNotExistException();
            }
            //创建排队记录
            BaseEnQueue baseEnQueue = new BaseEnQueue();
            try {
                BeanUtils.copyProperties(baseEnQueue,enQueue);
            } catch (Throwable e) {}
            queueId = save(num,condition,callId,baseEnQueue).getId();

            //lua脚本找坐席
            String agent = (String)redisCacheService.eval(Lua.LOOKUPAGENT,4,
                    CAs.getKey(condition.getId()),AgentState.getPrefixed(),
                    ExtensionState.getPrefixed(),AgentLock.getPrefixed(),
                    ""+AgentState.REG_EXPIRE,""+System.currentTimeMillis(),
                    CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,ExtensionState.Model.ENABLE_TRUE);
            if(logger.isDebugEnabled()){
                logger.debug("[{}][{}]排队结果:agent={},queueid={}",tenantId,appId,agent,queueId);
            }
            if(StringUtil.isEmpty(agent)){
                //没有找到可用坐席
                if(logger.isDebugEnabled()){
                    logger.debug("[{}][{}]callid={}发布排队超时事件",tenantId,appId,callId);
                }
                addCQS(conditionId,queueId);
                publishTimeoutEvent(condition,queueId,callId);
                String agent_idle = (String)redisCacheService.eval(Lua.LOOKUPAGENTFORIDLE,3,
                        CAs.getKey(condition.getId()),AgentState.getPrefixed(),
                        ExtensionState.getPrefixed(),
                        ""+AgentState.REG_EXPIRE,
                        ""+System.currentTimeMillis(),
                        CallCenterAgent.STATE_IDLE,ExtensionState.Model.ENABLE_TRUE);
                if(StringUtil.isNotEmpty(agent_idle)){
                    lookupQueue(tenantId,appId,conditionId,agent_idle);
                }
            }else{
                try{
                    EnQueueResult result = new EnQueueResult();
                    result.setExtension(appExtensionService.findById(agentState.getExtension(agent)));
                    result.setAgent(callCenterAgentService.findById(agent));
                    result.setBaseEnQueue(baseEnQueue);
                    deQueueService.success(tenantId,appId,callId,queueId,result);
                }catch (Throwable t1){
                    try{
                        agentState.setState(agent,CallCenterAgent.STATE_IDLE);
                    }catch (Throwable t2){
                        logger.info("设置坐席状态失败agent={}",agent,t2);
                    }
                    throw t1;
                }
            }
        }catch (Throwable e){
            logger.info("[{}][{}]callid={}排队找坐席出错:{}",tenantId,appId,callId,e.getMessage());
            deQueueService.fail(tenantId,appId,callId,e.getMessage(),queueId);
        }
    }

    /**
     * 坐席找排队
     * @param tenantId
     * @param appId
     * @param agent
     */
    @Override
    @Async
    public void lookupQueue(String tenantId, String appId,String conditionId, String agent){
        if(logger.isDebugEnabled()){
            logger.info("[{}][{}]开始坐席找排队agent={}",tenantId,appId,agent);
        }
        String queueId = (String)redisCacheService.eval(Lua.LOKUPQUEUE,6,
            ACs.getKey(agent),AgentState.getKey(agent),
            ExtensionState.getPrefixed(),AgentLock.getKey(agent),
            QueueLock.getPrefixed(),CQs.getPrefixed(),
            ""+AgentState.REG_EXPIRE,""+System.currentTimeMillis(),
                CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,
            conditionId==null?"":conditionId,ExtensionState.Model.ENABLE_TRUE);
        if(logger.isDebugEnabled()){
            logger.debug("[{}][{}]排队结果:agent={},queueid={}",tenantId,appId,agent,queueId);
        }
        if(queueId != null){
            //找到排队，修改排队状态
            try{
                CallCenterQueue queue = callCenterQueueService.findById(queueId);
                if(logger.isDebugEnabled()){
                    logger.debug("[{}][{}]排队结果:agent={},queueid={}",tenantId,appId,agent,queue.getId());
                }
                EnQueueResult result = new EnQueueResult();
                result.setExtension(appExtensionService.findById(agentState.getExtension(agent)));
                result.setAgent(callCenterAgentService.findById(agent));
                if(StringUtil.isNotEmpty(queue.getEnqueue())){
                    result.setBaseEnQueue(JSONUtil2.fromJson(queue.getEnqueue(),BaseEnQueue.class));
                }
                deQueueService.success(tenantId,appId,queue.getOriginCallId(),queueId,result);
            }catch (Throwable t1){
                logger.info("坐席找排队失败agent={}",agent,t1);
                try{
                    agentState.setState(agent,CallCenterAgent.STATE_IDLE);
                }catch (Throwable t2){
                    logger.info("设置坐席状态失败agent={}",agent,t2);
                }
            }
        }
    }
}
