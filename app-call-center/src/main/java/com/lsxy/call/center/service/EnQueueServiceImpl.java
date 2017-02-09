package com.lsxy.call.center.service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.*;
import com.lsxy.call.center.api.service.*;
import com.lsxy.call.center.api.states.statics.AQs;
import com.lsxy.call.center.batch.QueueBatchInserter;
import com.lsxy.call.center.api.states.lock.AgentLock;
import com.lsxy.call.center.api.states.lock.QueueLock;
import com.lsxy.call.center.api.states.state.AgentState;
import com.lsxy.call.center.api.states.state.ExtensionState;
import com.lsxy.call.center.api.states.statics.ACs;
import com.lsxy.call.center.api.states.statics.CAs;
import com.lsxy.call.center.api.states.statics.CQs;
import com.lsxy.call.center.utils.Lua;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.api.AgentNotExistException;
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
    private AQs aQs;

    @Autowired
    private MQService mqService;

    @Autowired
    private QueueBatchInserter queueBatchInserter;

    private CallCenterQueue save(String num,String tenantId,String appId,String agentId,String conditionId,Integer fetchTimeout,String callId,String conversationId,BaseEnQueue baseEnQueue,String type){
        CallCenterQueue queue = new CallCenterQueue();
        queue.setId(UUIDGenerator.uuid());
        queue.setTenantId(tenantId);
        queue.setAppId(appId);
        queue.setRelevanceId("");
        queue.setType(type);
        queue.setCondition(conditionId);
        queue.setStartTime(new Date());
        queue.setNum(num);
        queue.setOriginCallId(callId);
        queue.setAgent(agentId);
        queue.setFetchTimeOut(fetchTimeout);
        queue.setConversation(conversationId);
        queue.setEnqueue(JSONUtil.objectToJson(baseEnQueue));
        queueBatchInserter.put(queue);
        return queue;
    }

    private void addCQS(String conditionId,String queueId){
        cQs.add(conditionId, queueId);
    }

    private void addAQS(String agentId,String queueId,Integer priority){
        aQs.add(agentId, queueId,priority);
    }

    private void publishTimeoutEvent(String tenantId,String appId,String agentId,String conditionId,Integer queueTimeout,String queueId,String type,String callId,String conversationId){
        long start = 0;
        if(logger.isDebugEnabled()){
            start = System.currentTimeMillis();
        }
        mqService.publish(new EnqueueTimeoutEvent(
                agentId,
                conditionId,
                queueId,
                type,
                tenantId,
                appId,
                callId,conversationId,
                (queueTimeout == null ? 0 : queueTimeout) * 1000));
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
    public void lookupAgent(String tenantId, String appId,String num, String callId, EnQueue enQueue,String queueType,String conversationId){
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
            boolean lookupForCondition = enQueue.getRoute().getCondition()!=null;//指定坐席排队
            boolean lookupForAgent = enQueue.getRoute().getAgent() != null;//指定条件排队

            Condition condition = null;
            String conditionId = null;
            String agentName = null;
            CallCenterAgent agent = null;

            Integer fetchTimout = null;
            Integer queueTimout = null;
            Integer priority = null;

            if(lookupForCondition){
                conditionId = enQueue.getRoute().getCondition().getId();
                condition = conditionService.findById(conditionId);
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
                fetchTimout = condition.getFetchTimeout();
                queueTimout = condition.getQueueTimeout();
            }else if(lookupForAgent){
                agentName = enQueue.getRoute().getAgent().getName();
                agent = callCenterAgentService.get(appId,agentName);
                if(agent == null){
                    throw new AgentNotExistException();
                }
                if(!tenantId.equals(agent.getTenantId())){
                    throw new AgentNotExistException();
                }
                if(!appId.equals(agent.getAppId())){
                    throw new AgentNotExistException();
                }
                if(!agent.getChannel().equals(channel.getId())){
                    throw new AgentNotExistException();
                }
                fetchTimout = enQueue.getRoute().getAgent().getFetch_timeout();
                queueTimout = enQueue.getRoute().getAgent().getQueue_timeout();
                priority = enQueue.getRoute().getAgent().getPriority();
            }

            //创建排队记录
            BaseEnQueue baseEnQueue = new BaseEnQueue();
            try {
                BeanUtils.copyProperties(baseEnQueue,enQueue);
            } catch (Throwable e) {}

            if(lookupForCondition){
                queueId = save(num,tenantId,appId,agent.getId(),null,fetchTimout,callId,conversationId,baseEnQueue,queueType).getId();
            }else if(lookupForAgent){
                queueId = save(num,tenantId,appId,null,conditionId,fetchTimout,callId,conversationId,baseEnQueue,queueType).getId();
            }

            //lua脚本找坐席,默认排队规则为random，lru为最大空闲时长的优先
            String agentId = null;

            agentId = (String)redisCacheService.eval(EnQueue.CHOICE_LRU.equals(enQueue.getChoice())
                            ?Lua.LOOKUPAGENTLRU:Lua.LOOKUPAGENTRANDOM,4,
                    CAs.getKey(condition.getId()),AgentState.getPrefixed(),
                    ExtensionState.getPrefixed(),AgentLock.getPrefixed(),
                    ""+AgentState.REG_EXPIRE,""+System.currentTimeMillis(),
                    CallCenterAgent.STATE_IDLE,CallCenterAgent.STATE_FETCHING,ExtensionState.Model.ENABLE_TRUE,
                    agent == null?"":agent.getId());

            if(logger.isDebugEnabled()){
                logger.debug("[{}][{}]排队结果:agent={},queueid={}",tenantId,appId,agent,queueId);
            }
            if(StringUtil.isEmpty(agentId)){//没找到坐席
                //没有找到可用坐席
                if(logger.isDebugEnabled()){
                    logger.debug("[{}][{}]callid={}发布排队超时事件",tenantId,appId,callId);
                }
                if(lookupForAgent){
                    publishTimeoutEvent(tenantId,appId,agent.getId(),null,queueTimout,
                            queueId,queueType,callId,conversationId);
                    addAQS(agentId,queueId,priority);
                }else if(lookupForCondition){
                    publishTimeoutEvent(tenantId,appId,null,condition.getId(),queueTimout,
                            queueId,queueType,callId,conversationId);
                    addCQS(conditionId,queueId);
                    String agent_idle = (String)redisCacheService.eval(Lua.LOOKUPAGENTFORIDLE,3,
                            CAs.getKey(condition.getId()),AgentState.getPrefixed(),
                            ExtensionState.getPrefixed(),
                            ""+AgentState.REG_EXPIRE,
                            ""+System.currentTimeMillis(),
                            CallCenterAgent.STATE_IDLE,ExtensionState.Model.ENABLE_TRUE);
                    if(StringUtil.isNotEmpty(agent_idle)){
                        lookupQueue(tenantId,appId,conditionId,agent_idle);
                    }
                }
            }else{//找到坐席
                try{
                    if(agent == null){
                        agent = callCenterAgentService.findById(agentId);
                    }
                    EnQueueResult result = new EnQueueResult();
                    result.setExtension(appExtensionService.findById(agentState.getExtension(agentId)));
                    result.setAgent(agent);
                    result.setFetchTime(fetchTimout);
                    result.setBaseEnQueue(baseEnQueue);
                    deQueueService.success(tenantId,appId,callId,queueId,queueType,result,conversationId);
                }catch (Throwable t1){
                    try{
                        agentState.setState(agentId,CallCenterAgent.STATE_IDLE);
                    }catch (Throwable t2){
                        logger.info("设置坐席状态失败agent={}",agent,t2);
                    }
                    throw t1;
                }
            }
        }catch (Throwable e){
            logger.error(String.format("[%s][%s]callid=%s排队找坐席出错",tenantId,appId,callId),e);
            deQueueService.fail(tenantId,appId,callId,e.getMessage(),queueId,queueType,conversationId);
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
        String queueId = (String)redisCacheService.eval(Lua.LOKUPQUEUE,7,
            ACs.getKey(agent),AgentState.getKey(agent),AQs.getKey(agent),
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
            CallCenterQueue queue = null;
            try{
                queue = callCenterQueueService.findById(queueId);
                if(logger.isDebugEnabled()){
                    logger.debug("[{}][{}]排队结果:agent={},queueid={}",tenantId,appId,agent,queue.getId());
                }
                EnQueueResult result = new EnQueueResult();
                result.setExtension(appExtensionService.findById(agentState.getExtension(agent)));
                result.setAgent(callCenterAgentService.findById(agent));
                if(StringUtil.isNotEmpty(queue.getEnqueue())){
                    result.setBaseEnQueue(JSONUtil2.fromJson(queue.getEnqueue(),BaseEnQueue.class));
                }
                result.setFetchTime(queue.getFetchTimeOut());
                deQueueService.success(tenantId,appId,queue.getOriginCallId(),queueId,queue.getType(),result,queue.getConversation());
            }catch (Throwable t1){
                logger.info("坐席找排队失败agent={}",agent,t1);
                try{
                    agentState.setState(agent,CallCenterAgent.STATE_IDLE);
                }catch (Throwable t2){
                    logger.info("设置坐席状态失败agent={}",agent,t2);
                }
                if(queue != null){
                    deQueueService.fail(tenantId,appId,queue.getOriginCallId(),t1.getMessage(),queueId,queue.getType(),queue.getConversation());
                }
            }
        }
    }
}
