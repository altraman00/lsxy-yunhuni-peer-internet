package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 双向回拨
 * Created by zhangxb on 2016/7/19.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_callback")
public class VoiceCallback extends IdEntity {
    private Date startTime;//发起时间
    private Date endTime;//结束时间
    private String formNum;//发起方
    private String toNum;//接收方
    private Integer endMode;//结束方式
    @Column( name = "startTime")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    @Column( name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    @Column( name = "form_num")
    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }
    @Column( name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }
    @Column( name = "end_mode")
    public Integer getEndMode() {
        return endMode;
    }

    public void setEndMode(Integer endMode) {
        this.endMode = endMode;
    }
}
