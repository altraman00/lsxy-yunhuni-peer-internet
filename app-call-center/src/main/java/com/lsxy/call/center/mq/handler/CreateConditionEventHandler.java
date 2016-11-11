package com.lsxy.call.center.mq.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.call.center.api.service.DeQueueService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.CreateConditionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class CreateConditionEventHandler implements MQMessageHandler<CreateConditionEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CreateConditionEventHandler.class);

    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private ConditionService conditionService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private DeQueueService deQueueService;

    @Override
    public void handleMessage(CreateConditionEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.CreateConditionEvent{}",message.toJson());
        }
        if(message.getId() == null ||
                message.getTenantId() == null ||
                message.getAppId() == null){
            logger.info("处理CallCenter.CreateConditionEvent出错，参数错误！");
            return;
        }
        Condition condition = conditionService.findById(message.getId());
        if(condition == null){
            logger.info("处理CallCenter.CreateConditionEvent出错，条件不存在！");
            return;
        }
        //初始化CAs ACs CQs

    }
}
