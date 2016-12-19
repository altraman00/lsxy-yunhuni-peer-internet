package com.lsxy.framework.mq.events.portal;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * 删除录音文件事件
 * Created by zhangxb on 2016/9/12.
 */
public class VoiceFileRecordDeleteEvent extends AbstractMQEvent {
    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_APP_PORTAL;
    }

    public VoiceFileRecordDeleteEvent() {
    }

}
