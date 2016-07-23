package com.lsxy.framework.mq.test.events;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by Tandy on 2016/7/21.
 */
public class TestEvent extends AbstractMQEvent{



    @Override
    public String getTopicName() {
        return "yunhuni_topic_framework_tenant";
    }
}
