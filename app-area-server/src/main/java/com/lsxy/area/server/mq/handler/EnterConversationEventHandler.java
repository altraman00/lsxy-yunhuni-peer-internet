package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.agentserver.EnterConversationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by liuws on 2016/9/13.
 */
@Component
public class EnterConversationEventHandler implements MQMessageHandler<EnterConversationEvent> {

    private static final Logger logger = LoggerFactory.getLogger(EnterConversationEventHandler.class);

    @Autowired
    private ConversationService conversationService;

    @Override
    public void handleMessage(EnterConversationEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理加入交谈事件{}",message.toJson());
        }
        try {
            conversationService.join(message.getCallId(),message.getConversationId(),
                    message.getMaxDuration(),message.getPlayFile(),message.getVoiceMode());
        } catch (YunhuniApiException e) {
            logger.info("加入交谈失败{}",message);
            conversationService.logicExit(message.getConversationId(),message.getCallId());
        }
    }
}
