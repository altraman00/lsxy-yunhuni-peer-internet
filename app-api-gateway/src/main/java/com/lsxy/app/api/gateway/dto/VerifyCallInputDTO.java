package com.lsxy.app.api.gateway.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.StringUtils;

import javax.validation.GroupSequence;
import javax.validation.constraints.*;

/**
 * 语音验证码
 * Created by liuws on 2016/8/24.
 */
@GroupSequence({VerifyCallInputDTO.class,VerifyCallInputDTO.Second.class})
public class VerifyCallInputDTO extends CommonDTO{

    @JsonProperty("from")
    @Size(max = 128)
    private String from;//主叫号码

    @JsonProperty("to")
    @NotNull
    @Size(min = 1,max = 128)
    private String to;//被叫号码

    @JsonProperty("max_dial_duration")
    @Min(1)
    @Max(60 * 60 * 6)
    private Integer maxDialDuration;//最大拨号等待时间（秒）

    @JsonProperty("play_file")
    @Size(max = 512)
    private String playFile;

    @JsonProperty("verify_code")
    @Pattern(regexp = "^([0-9]{1,12})?$")
    private String verifyCode;

    @JsonProperty("repeat")
    @Min(0)
    @Max(10)
    private Integer repeat;

    @JsonProperty("user_data")
    @Size(max = 128)
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


    /**javax.valid 不支持组合校验只能使用这种方式了**/
    @AssertTrue(groups = Second.class,message = "play_file，verify_code不能同时为null")
    public boolean getValidSuccess() {
        if(StringUtils.isBlank(this.getPlayFile()) && StringUtils.isBlank(this.getVerifyCode())){
            return false;
        }
        return true;
    }

    public interface Second{

    }
}

