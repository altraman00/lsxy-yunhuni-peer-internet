package com.lsxy.call.center.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ModifyConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class ModifyConditionEventHandler implements MQMessageHandler<ModifyConditionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ModifyConditionEventHandler.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Override
    public void handleMessage(ModifyConditionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.ModifyConditionEvent{}",message.toJson());
        }
    }
}
