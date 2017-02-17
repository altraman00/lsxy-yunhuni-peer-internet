package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.CallCenterIncrCostEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

@Component
public class CallCenterIncrCostEventHandler implements MQMessageHandler<CallCenterIncrCostEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterIncrCostEventHandler.class);

    @Autowired
    private CallCenterService callCenterService;

    @Override
    public void handleMessage(CallCenterIncrCostEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenterIncrCostEvent{}",message.toJson());
        }
        callCenterService.incrCost(message.getCallCenterId(),message.getCost());
    }
}
