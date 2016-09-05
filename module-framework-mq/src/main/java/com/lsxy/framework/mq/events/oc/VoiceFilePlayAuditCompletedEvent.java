package com.lsxy.framework.mq.events.oc;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功  完成
 */
public class VoiceFilePlayAuditCompletedEvent extends AbstractMQEvent{


    public VoiceFilePlayAuditCompletedEvent(){
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_oc";
    }
}
