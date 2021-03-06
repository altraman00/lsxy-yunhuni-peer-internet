package com.lsxy.call.center.mq.handler;

import com.lsxy.call.center.api.model.CallCenterConversationMember;
import com.lsxy.call.center.api.service.CallCenterConversationMemberService;
import com.lsxy.framework.core.exceptions.api.ConversationMemberNotExistException;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.callcenter.ConversationMemberExitEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.Date;

@Component
public class ConversationMemberExitEventHandler implements MQMessageHandler<ConversationMemberExitEvent> {

    private static final Logger logger = LoggerFactory.getLogger(ConversationMemberExitEventHandler.class);

    /**超过半个小时的消息直接丢弃**/
    private static final long expired = 1000 * 60 * 30;

    @Autowired
    private CallCenterConversationMemberService callCenterConversationMemberService;

    @Override
    public void handleMessage(ConversationMemberExitEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理CallCenter.ConversationMemberExitEvent{}",message.toJson());
        }
        if(System.currentTimeMillis() - message.getTimestamp() <= expired){
            CallCenterConversationMember member = callCenterConversationMemberService.findOne(message.getConversationId(),message.getCallId());
            if(member == null){
                //member为null，消息消费失败，会重新消费
                throw new RuntimeException(
                        new ConversationMemberNotExistException(new ExceptionContext().put("交谈成员尚未就绪",message.toJson())));
            }
            CallCenterConversationMember update = new CallCenterConversationMember();
            update.setEndTime(new Date(message.getTimestamp()));
            callCenterConversationMemberService.update(member.getId(),update);
        }

    }
}
