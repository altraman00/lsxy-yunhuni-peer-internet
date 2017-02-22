package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 退出会议
 * Created by liuws on 2016/8/26.
 */
public class ConfQuitInputDTO extends CommonDTO{

    @JsonProperty("call_id")
    @NotNull
    @Size(min = 1,max = 128)
    private String callId;//呼叫id

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }
}
