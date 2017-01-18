package com.lsxy.app.backend.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.portal.RechargeSuccessEvent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liups on 2017/1/13.
 */
@Component
public class RechargeSuccessHandler implements MQMessageHandler<RechargeSuccessEvent> {
    private static final Logger logger = LoggerFactory.getLogger(ModifyEmailSuccessEventHandler.class);

    @Autowired
    ResourcesRentService resourcesRentService;

    @Override
    public void handleMessage(RechargeSuccessEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理充值成功事件,tenantId:{}",message.getTenantId());
        }
        resourcesRentService.payResourcesRent(message.getTenantId());

    }
}
