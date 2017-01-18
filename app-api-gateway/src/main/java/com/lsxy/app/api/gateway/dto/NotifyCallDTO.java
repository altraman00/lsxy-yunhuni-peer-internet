package com.lsxy.app.api.gateway.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通知外呼
 * Created by liups on 2016/8/26.
 */
public class NotifyCallDTO implements Serializable {
    private String from;        //主叫号码
    @NotNull
    private String to;          //被叫号码
    private String play_file;     //通知放音文件(列表)
    private List<List<Object>> play_content; //话音文件播放内容
    @Min(0)
    @Max(10)
    private Integer repeat = 1;     //重复播放次数
    @Min(1)
    @Max(5 * 60)
    private Integer max_dial_duration = 45;  //最大拨号等待时间（秒）
    @Size(max = 128)
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

    public String getPlay_file() {
        return play_file;
    }

    public void setPlay_file(String play_file) {
        this.play_file = play_file;
    }

    public List<List<Object>> getPlay_content() {
        return play_content;
    }

    public void setPlay_content(List<List<Object>> play_content) {
        this.play_content = play_content;
    }

    public Integer getRepeat() {
        return repeat;
    }

    public void setRepeat(Integer repeat) {
        this.repeat = repeat;
    }

    public Integer getMax_dial_duration() {
        return max_dial_duration;
    }

    public void setMax_dial_duration(Integer max_dial_duration) {
        this.max_dial_duration = max_dial_duration;
    }

    public String getUser_data() {
        return user_data;
    }

    public void setUser_data(String user_data) {
        this.user_data = user_data;
    }
}
