package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfRecordInputDTO extends CommonDTO{

    @JsonProperty("max_duration ")
    private Integer maxDuration;

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }
}
