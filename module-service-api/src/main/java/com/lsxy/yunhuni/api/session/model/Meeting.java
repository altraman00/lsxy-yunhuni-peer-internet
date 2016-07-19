package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 会议
 * Created by zhangxb on 2016/7/19.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_meeting")
public class Meeting  extends IdEntity {
    private CallSession session;//所属会议
    private String formNum;//发起人
    private Date startTime;//发起时间
    @OneToMany
    @JoinColumn(name = "session_id")
    public CallSession getSession() {
        return session;
    }

    public void setSession(CallSession session) {
        this.session = session;
    }
    @Column(name = "form_num")
    public String getFormNum() {
        return formNum;
    }

    public void setFormNum(String formNum) {
        this.formNum = formNum;
    }
    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
