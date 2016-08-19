package com.lsxy.framework.mq.events.apigw.test;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by tandy on 16/8/10.
 */
public class TestEchoRequestEvent extends AbstractMQEvent{
    private String requestId;
    private String name;

    public TestEchoRequestEvent(){}

    public TestEchoRequestEvent(String requestId,String name){
        this.requestId = requestId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_test";
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setName(String name) {
        this.name = name;
    }
}
