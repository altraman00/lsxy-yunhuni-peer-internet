package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2016/8/24.
 */
public class ConfCreateInputDTO extends CommonDTO{

    @JsonProperty("max_duration")
    @NotNull
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxDuration;// 呼叫最大接通时间（秒）

    @JsonProperty("max_parts")
    @Min(1)
    private Integer maxParts;// 最大与会方数

    @JsonProperty("recording")
    private Boolean recording;// 是否自动启动录音

    @JsonProperty("auto_hangup")
    private Boolean  autoHangup;// 会议结束自动挂断与会方

    @JsonProperty("bgm_file")
    @Size(max = 128)
    private String bgmFile;// 背景音文件

    @JsonProperty("user_data")
    @Size(max = 128)
    private String userData;// 用户数据


    public Integer getMaxDuration() {
        return maxDuration;
    }


    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getMaxParts() {
        return maxParts;
    }

    public void setMaxParts(Integer maxParts) {
        this.maxParts = maxParts;
    }

    public Boolean getRecording() {
        return recording;
    }

    public void setRecording(Boolean recording) {
        this.recording = recording;
    }

    public Boolean getAutoHangup() {
        return autoHangup;
    }

    public void setAutoHangup(Boolean autoHangup) {
        this.autoHangup = autoHangup;
    }

    public String getBgmFile() {
        return bgmFile;
    }

    public void setBgmFile(String bgmFile) {
        this.bgmFile = bgmFile;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }
}
