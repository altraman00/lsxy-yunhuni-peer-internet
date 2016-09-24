package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 语音通知
 * Created by liups on 2016/9/6.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_notify_call")
public class NotifyCall extends IdEntity {
    private Date startTime;//发起时间
    private Date answerTime;//接听时间
    private Date endTime;//结束时间
    private String fromNum;//发起方
    private String toNum;//接收方
    private String hangupSide;
    private String resId;//资源ID

    public NotifyCall() {
    }

    public NotifyCall(String fromNum, String toNum) {
        this.fromNum = fromNum;
        this.toNum = toNum;
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

    @Column( name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }

    @Column( name = "hangup_side")
    public String getHangupSide() {
        return hangupSide;
    }

    public void setHangupSide(String hangupSide) {
        this.hangupSide = hangupSide;
    }

    @Column( name = "res_id")
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}
