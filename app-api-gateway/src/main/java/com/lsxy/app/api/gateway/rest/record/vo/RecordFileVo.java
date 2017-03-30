package com.lsxy.app.api.gateway.rest.record.vo;

import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;

/**
 * Created by liups on 2017/3/17.
 */
public class RecordFileVo {
    private String id;
    private String date;
    private Long duration;
    private Long size;

    public RecordFileVo() {
    }

    public RecordFileVo(VoiceFileRecord record){
        this.id = record.getId();
        this.date = DateUtils.getDate(record.getCreateTime(),"yyyy-MM-dd HH:mm:ss");
        this.duration = record.getCostTimeLong();
        this.size = record.getSize();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
