package com.lsxy.app.api.gateway.dto;

import java.util.List;
import java.io.Serializable;

/**
 * 语音验证码
 * Created by liups on 2016/9/1.
 */
@Deprecated
public class CaptchaCallDTO implements Serializable {
    private String from;
    private String to;
    private String verify_code;
    private Integer max_dial_duration;   //最大拨号等待时间（秒）
    private Integer max_keys;
    private List<String> files;
    private String user_data;

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

    public String getVerify_code() {
        return verify_code;
    }

    public void setVerify_code(String verify_code) {
        this.verify_code = verify_code;
    }

    public Integer getMax_dial_duration() {
        return max_dial_duration;
    }

    public void setMax_dial_duration(Integer max_dial_duration) {
        this.max_dial_duration = max_dial_duration;
    }

    public Integer getMax_keys() {
        return max_keys;
    }

    public void setMax_keys(Integer max_keys) {
        this.max_keys = max_keys;
    }

    public List<String> getFiles() {
        return files;
    }

    public void setFiles(List<String> files) {
        this.files = files;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }
}
