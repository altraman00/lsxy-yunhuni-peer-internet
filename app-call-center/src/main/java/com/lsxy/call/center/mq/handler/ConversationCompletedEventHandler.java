package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ConversationCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class ConversationCompletedEventHandler implements MQMessageHandler<ConversationCompletedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ConversationCompletedEventHandler.class);

    @Autowired
    private CallCenterConversationService callCenterConversationService;

    @Override
    public void handleMessage(ConversationCompletedEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.ConversationCompletedEvent{}",message.toJson());
        }
        CallCenterConversation updateCallCenterConversation = new CallCenterConversation();
        updateCallCenterConversation.setEndTime(new Date(message.getTimestamp()));
        updateCallCenterConversation.setState(CallCenterConversation.STATE_COMPLETED);
        callCenterConversationService.update(message.getConversationId(),updateCallCenterConversation);
    }
}
