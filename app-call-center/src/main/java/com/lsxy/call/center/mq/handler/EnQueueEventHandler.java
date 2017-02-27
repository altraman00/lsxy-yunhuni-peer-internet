package com.lsxy.call.center.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.api.states.lock.QueueLock;
import com.lsxy.call.center.api.states.statics.AQs;
import com.lsxy.call.center.api.states.statics.CQs;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.EnqueueTimeoutEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class EnQueueEventHandler implements MQMessageHandler<EnqueueTimeoutEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EnQueueEventHandler.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private CQs cQs;

    @Autowired
    private AQs aQs;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Override
    public void handleMessage(EnqueueTimeoutEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.EnQueueEvent{}",message.toJson());
        }
        QueueLock queueLock = new QueueLock(redisCacheService,message.getQueueId());
        if(queueLock.lock()){
            if(message.getConditionId()!=null){
                if(cQs.exists(message.getConditionId(),message.getQueueId())){//排队超时
                    cQs.remove(message.getConditionId(),message.getQueueId());
                    try{
                        deQueueService.timeout(message.getTenantId(),message.getAppId(),message.getCallId(),message.getQueueId(),message.getType(),message.getConversationId());
                    }catch (Throwable t){
                        logger.info("[{}][{}]排队超时事件{}",message.getTenantId(),message.getAppId(),t);
                    }
                }
            }else if(message.getAgentId()!=null){
                if(aQs.exists(message.getAgentId(),message.getQueueId())){//排队超时
                    aQs.remove(message.getAgentId(),message.getQueueId());
                    try{
                        deQueueService.timeout(message.getTenantId(),message.getAppId(),message.getCallId(),message.getQueueId(),message.getType(),message.getConversationId());
                    }catch (Throwable t){
                        logger.info("[{}][{}]排队超时事件{}",message.getTenantId(),message.getAppId(),t);
                    }
                }
            }

        }

    }
}
