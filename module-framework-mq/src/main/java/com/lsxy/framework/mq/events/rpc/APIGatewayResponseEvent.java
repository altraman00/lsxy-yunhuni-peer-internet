package com.lsxy.framework.mq.events.rpc;

import com.lsxy.framework.mq.api.AbstractMQEvent;
import com.lsxy.framework.mq.api.MQEvent;

/**
 * Created by tandy on 16/8/7.
 *
 */
public class APIGatewayResponseEvent extends AbstractMQEvent{
    private String httpRequestId;

    public APIGatewayResponseEvent(String httpRequestId) {
        this.httpRequestId = httpRequestId;
    }

    public String getHttpRequestId() {
        return httpRequestId;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_apigw_response";
    }
}
