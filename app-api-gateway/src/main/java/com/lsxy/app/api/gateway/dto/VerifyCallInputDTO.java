package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by liuws on 2016/8/24.
 */
public class VerifyCallInputDTO extends CommonDTO{

    @JsonProperty("from")
    private String from;//主叫号码

    @JsonProperty("to")
    private String to;//被叫号码

    @JsonProperty("max_dial_duration")
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("play_file")
    private String playFile;

    @JsonProperty("verify_code")
    private String verifyCode;

    @JsonProperty("repeat")
    private Integer repeat;

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

    public Integer getMaxDialDuration() {
        return maxDialDuration;
    }

    public void setMaxDialDuration(Integer maxDialDuration) {
        this.maxDialDuration = maxDialDuration;
    }

    public String getPlayFile() {
        return playFile;
    }

    public void setPlayFile(String playFile) {
        this.playFile = playFile;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }


    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
