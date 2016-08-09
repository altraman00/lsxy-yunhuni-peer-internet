package com.lsxy.framework.mq.events.apigw;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import java.util.Map;

/**
 * Created by tandy on 16/8/7.
 */
public class APIGatewayRequestEvent extends AbstractMQEvent{

    private String requestId;

    private String method;

    private Map<String,Object> params;


    public APIGatewayRequestEvent(String requestId, String method, Map<String, Object> parameterMap){
        this.requestId = requestId;
        this.method = method;
        this.params = parameterMap;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    public String getRequestId() {
        return requestId;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_apigw_request";
    }
}
