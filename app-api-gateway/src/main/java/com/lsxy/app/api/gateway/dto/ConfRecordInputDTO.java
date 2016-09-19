package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by liuws on 2016/8/26.
 */
public class ConfRecordInputDTO extends CommonDTO{

    @JsonProperty("max_duration ")
    @Min(1)
    @Max(60*60*6)
    private Integer maxDuration;

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }
}
