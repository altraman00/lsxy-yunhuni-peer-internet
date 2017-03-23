package com.lsxy.framework.mq.events.msg;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import static com.lsxy.framework.mq.topic.MQTopicConstants.TOPIC_APP_OC;

/**
 * Created by liups on 2017/3/23.
 */
public class TemplatePassedEvent extends AbstractMQEvent {
    private String tempId;

    @Override
    public String getTopicName() {
        return TOPIC_APP_OC;
    }

    public TemplatePassedEvent() {
    }

    public TemplatePassedEvent(String tempId) {
        this.tempId = tempId;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }
}
