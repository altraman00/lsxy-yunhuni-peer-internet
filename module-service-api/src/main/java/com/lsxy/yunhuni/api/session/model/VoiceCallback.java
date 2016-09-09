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
    private Date answerTime;//第一方应答时间
    private Date connectTime;//第二方应答时间
    private Date endTime;//结束时间
    private String fromNum;//发起方
    private String from2Num;//发起方
    private String toNum;//接收方
    private String to2Num;//接收方2
    private String handupSide;
    private String resId;//资源ID

    public VoiceCallback() {
    }

    public VoiceCallback(String id,String fromNum, String from2Num, String toNum,String to2Num) {
        this.id = id;
        this.fromNum = fromNum;
        this.from2Num = from2Num;
        this.toNum = toNum;
        this.to2Num = to2Num;
    }

    @Column( name = "start_time")
    public Date getStartTime() {
        return startTime;
    }


    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column( name = "answer_time")
    public Date getAnswerTime() {
        return answerTime;
    }

    public void setAnswerTime(Date answerTime) {
        this.answerTime = answerTime;
    }

    @Column( name = "connect_time")
    public Date getConnectTime() {
        return connectTime;
    }

    public void setConnectTime(Date connectTime) {
        this.connectTime = connectTime;
    }

    @Column( name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    @Column( name = "from_num")
    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    @Column( name = "from2_num")
    public String getFrom2Num() {
        return from2Num;
    }

    public void setFrom2Num(String from2Num) {
        this.from2Num = from2Num;
    }

    @Column( name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }

    @Column( name = "to2_num")
    public String getTo2Num() {
        return to2Num;
    }

    public void setTo2Num(String to2Num) {
        this.to2Num = to2Num;
    }

    @Column( name = "handup_side")
    public String getHandupSide() {
        return handupSide;
    }

    public void setHandupSide(String handupSide) {
        this.handupSide = handupSide;
    }

    @Column( name = "res_id")
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}
