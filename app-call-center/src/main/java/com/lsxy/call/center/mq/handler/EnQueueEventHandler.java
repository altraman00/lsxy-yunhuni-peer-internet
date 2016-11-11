package com.lsxy.call.center.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.call.center.states.statics.CQs;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.EnqueueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class EnQueueEventHandler implements MQMessageHandler<EnqueueEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EnQueueEventHandler.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private CQs cQs;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Override
    public void handleMessage(EnqueueEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.EnQueueEvent{}",message.toJson());
        }
        if(cQs.exists(message.getConditionId(),message.getQueueId())){//排队超时
            cQs.remove(message.getConditionId(),message.getQueueId());
            deQueueService.timeout(message.getTenantId(),message.getAppId(),message.getCallId());
        }
    }
}
