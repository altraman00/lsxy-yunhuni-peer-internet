package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2017/1/10.
 */
public class InviteAgentInputDTO extends CommonDTO {

    @NotNull
    @Size(max = 1024)
    @JsonProperty("enqueue")
    private String enqueue;

    @Min(1)
    @Max(4)
    @JsonProperty("mode")
    private Integer mode;

    public String getEnqueue() {
        return enqueue;
    }

    public void setEnqueue(String enqueue) {
        this.enqueue = enqueue;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
