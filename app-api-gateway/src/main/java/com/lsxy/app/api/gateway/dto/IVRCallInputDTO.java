package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuws on 2016/8/24.
 */
public class IVRCallInputDTO extends CommonDTO{

    @JsonProperty("from")
    private String from;//主叫号码

    @JsonProperty("to")
    private String to;//被叫号码

    @JsonProperty("max_call_duration")
    private Integer maxCallDuration;//最大会议时间（秒）

    @JsonProperty("max_dial_duration")
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("user_data")
    private String userData;

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

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
