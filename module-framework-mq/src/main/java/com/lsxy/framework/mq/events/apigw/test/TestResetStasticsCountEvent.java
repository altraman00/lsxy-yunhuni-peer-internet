package com.lsxy.framework.mq.events.apigw.test;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by tandy on 16/8/10.
 */
public class TestResetStasticsCountEvent extends AbstractMQEvent{

    @Override
    public String getTopicName() {
        return "yunhuni_topic_test";
    }
}
