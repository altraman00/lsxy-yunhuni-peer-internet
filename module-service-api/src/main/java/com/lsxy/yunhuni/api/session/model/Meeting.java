package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * 会议
 * Created by zhangxb on 2016/7/19.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_meeting")
public class Meeting  extends IdEntity {
    private List<CallSession> sessions;//所属会议
    private String fromNum;//发起人
    private Date startTime;//发起时间
    @OneToMany
    @JoinColumn(name = "session_id")
    public List<CallSession> getSessions() {
        return sessions;
    }
    public void setSessions(List<CallSession> sessions) {
        this.sessions = sessions;
    }
    @Column(name = "from_num")
    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }



    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
}
