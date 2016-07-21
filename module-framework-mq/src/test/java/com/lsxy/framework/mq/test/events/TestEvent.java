package com.lsxy.framework.mq.test.events;

import com.lsxy.framework.mq.AbstractMQEvent;
import com.lsxy.framework.mq.MQEvent;

/**
 * Created by Tandy on 2016/7/21.
 */
public class TestEvent extends AbstractMQEvent{


    @Override
    public String getTopicName() {
        return "test_yunhuni_topic_framework_tenant";
    }
}
