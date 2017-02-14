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
public class InviteOutInputDTO extends CommonDTO {

    @JsonProperty("from")
    @Size(max = 128)
    private String from;//主叫号码

    @JsonProperty("to")
    @NotNull
    @Size(min = 1,max = 128)
    private String to;//被叫号码

    @JsonProperty("max_call_duration")
    @NotNull
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxCallDuration;//最大通话时间（秒）

    @JsonProperty("max_dial_duration")
    @Min(1)
    @Max(60 * 5)
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("mode")
    @Min(1)
    @Max(4)
    private Integer mode;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Integer getMaxCallDuration() {
        return maxCallDuration;
    }

    public void setMaxCallDuration(Integer maxCallDuration) {
        this.maxCallDuration = maxCallDuration;
    }

    public Integer getMaxDialDuration() {
        return maxDialDuration;
    }

    public void setMaxDialDuration(Integer maxDialDuration) {
        this.maxDialDuration = maxDialDuration;
    }

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }
}
