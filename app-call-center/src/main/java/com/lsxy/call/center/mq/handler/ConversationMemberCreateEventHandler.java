package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ConversationMemberCreateEvent;
import com.lsxy.framework.mq.events.callcenter.ConversationMemberExitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class ConversationMemberCreateEventHandler implements MQMessageHandler<ConversationMemberCreateEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ConversationMemberCreateEventHandler.class);

    @Autowired
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Override
    public void handleMessage(ConversationMemberCreateEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.ConversationMemberCreateEvent{}",message.toJson());
        }
        callCenterConversationMemberService.save(JSONUtil2.fromJson(message.getMember(),CallCenterConversationMember.class));
    }
}
