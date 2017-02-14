package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

import java.util.Date;

/**
 * Created by liuws on 2016/11/9.
 */
public class ConversationMemberExitEvent extends AbstractMQEvent {

    private String conversationId;

    private String callId;

    public ConversationMemberExitEvent(){
    }

    public ConversationMemberExitEvent(String conversationId, String callId){
        this.conversationId = conversationId;
        this.callId = callId;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

}
