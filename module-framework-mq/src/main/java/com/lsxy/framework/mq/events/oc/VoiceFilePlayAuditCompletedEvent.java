package com.lsxy.framework.mq.events.oc;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import static com.lsxy.framework.mq.topic.MQTopicConstants.TOPIC_APP_OC;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功  完成
 */
public class VoiceFilePlayAuditCompletedEvent extends AbstractMQEvent{
    private String key;

    public VoiceFilePlayAuditCompletedEvent(){
    }

    public VoiceFilePlayAuditCompletedEvent(String key) {
        this.key = key;
    }

    @Override
    public String getTopicName() {
        return TOPIC_APP_OC;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
