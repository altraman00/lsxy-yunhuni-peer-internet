package com.lsxy.framework.mq.events.apigw;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by tandy on 16/8/7.
 */
public class APIGatewayRequestEvent extends AbstractMQEvent{

    private String requestId;

    public APIGatewayRequestEvent(String requestId){
        this.requestId = requestId;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_apigw_request";
    }
}
