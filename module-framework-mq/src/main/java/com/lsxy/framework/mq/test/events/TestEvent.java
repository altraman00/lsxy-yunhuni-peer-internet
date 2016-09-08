package com.lsxy.framework.mq.test.events;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.topic.MQTopicConstants;

/**
 * Created by Tandy on 2016/7/21.
 */
public class TestEvent extends AbstractMQEvent{

    @Override
    public String getTopicName() {
        return MQTopicConstants.TOPIC_TEST;
    }
}
