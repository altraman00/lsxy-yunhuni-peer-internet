package com.lsxy.framework.mq.events.callcenter;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by liuws on 2016/11/9.
 */
public class ConversationMemberCreateEvent extends AbstractMQEvent {

    private String member;

    public ConversationMemberCreateEvent(){
    }

    public ConversationMemberCreateEvent(String member){
        this.member = member;
    }

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_CALL_CENTER;
    }

    public String getMember() {
        return member;
    }

    public void setMember(String member) {
        this.member = member;
    }
}
