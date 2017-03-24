package com.lsxy.framework.mq.events.msg;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import static com.lsxy.framework.mq.topic.MQTopicConstants.TOPIC_APP_OC;

/**
 * Created by liups on 2017/3/23.
 */
public class MsgRequestCompletedEvent extends AbstractMQEvent {

    private String msgKey;

    @Override
    public String getTopicName() {
        return TOPIC_APP_OC;
    }

    public MsgRequestCompletedEvent() {
    }

    public MsgRequestCompletedEvent(String msgKey) {
        this.msgKey = msgKey;
    }

    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
    }
}
