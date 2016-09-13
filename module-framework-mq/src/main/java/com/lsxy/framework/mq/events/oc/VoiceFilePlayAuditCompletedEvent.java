package com.lsxy.framework.mq.events.oc;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import static com.lsxy.framework.mq.topic.MQTopicConstants.TOPIC_APP_OC;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功  完成
 */
public class VoiceFilePlayAuditCompletedEvent extends AbstractMQEvent{


    public VoiceFilePlayAuditCompletedEvent(){
    }

    @Override
    public String getTopicName() {
        return TOPIC_APP_OC;
    }
}
