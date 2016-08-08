package com.lsxy.framework.mq.events.apigw;

import com.lsxy.framework.mq.api.AbstractMQEvent;

/**
 * Created by tandy on 16/8/7.
 *
 */
public class APIGatewayResponseEvent extends AbstractMQEvent{
    private String httpRequestId;
    private long requestTimestamp;

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }

    private Object data;
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public APIGatewayResponseEvent(String httpRequestId) {
        this.httpRequestId = httpRequestId;
    }

    public String getHttpRequestId() {
        return httpRequestId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_apigw_response";
    }
}
