package com.lsxy.framework.mq.events.apigw.test;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import java.util.Date;

/**
 * Created by tandy on 16/8/10.
 */
public class TestEchoResponseEvent extends AbstractMQEvent{
    private String requestId;
    private String message;

    private long requestTimestamp;

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    public TestEchoResponseEvent(String requestId, String name){
        this.requestId = requestId;
        this.message = "hello :" + name;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_apigw_response";
    }

    public String getRequestId() {
        return requestId;
    }

    public String getMessage() {
        return message;
    }
}
