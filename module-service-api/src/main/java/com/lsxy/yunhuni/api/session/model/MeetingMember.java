package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * 会议成员
 * Created by zhangxb on 2016/7/19.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_meeting_member")
public class MeetingMember extends IdEntity {
    public static final int JOINTYPE_INVITE = 1;
    public static final int JOINTYPE_CALL = 2;

    private String tenantId;//所属租户
    private String  appId;//所属应用
    private String number;//参与者号码
    private Date joinTime;//加入时间
    private Integer joinType;//加入类型1.邀请加入2.呼入加入
    private Meeting meeting;//所属会议
    private CallSession session;//关联会话
    private String resId;

    @Column( name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column( name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column( name = "number" )
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @Column( name = "join_time" )
    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }
    @Column( name = "join_type" )
    public Integer getJoinType() {
        return joinType;
    }

    public void setJoinType(Integer joinType) {
        this.joinType = joinType;
    }
    @ManyToOne
    @JoinColumn( name = "meeting_id" )
    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }
    @OneToOne
    @JoinColumn( name = "session_id" )
    public CallSession getSession() {
        return session;
    }

    public void setSession(CallSession session) {
        this.session = session;
    }


    @Column( name = "res_id" )
    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }
}
