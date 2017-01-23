package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.NotNull;

/**
 * Created by liuws on 2017/1/16.
 */
public class AgentCallAgentInputDTO extends CommonDTO {

    @NotNull
    @JsonProperty("enqueue")
    private String enqueue;

    public String getEnqueue() {
        return enqueue;
    }

    public void setEnqueue(String enqueue) {
        this.enqueue = enqueue;
    }
}
