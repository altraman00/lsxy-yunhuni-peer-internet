package com.lsxy.framework.mq.events.apigw.test;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by tandy on 16/8/10.
 */
public class TestResetStasticsCountEvent extends AbstractMQEvent{

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_TEST;
    }
}
