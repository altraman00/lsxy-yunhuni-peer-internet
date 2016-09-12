package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfQuitInputDTO extends CommonDTO{
    @JsonProperty("call_id")
    private String callId;//呼叫id

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
