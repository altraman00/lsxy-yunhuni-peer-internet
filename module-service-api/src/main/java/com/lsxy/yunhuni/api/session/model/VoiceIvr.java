package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liuws on 2016/9/7.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_ivr")
public class VoiceIvr extends IdEntity {
    public static final Integer IVR_TYPE_INCOMING = 1;
    public static final Integer IVR_TYPE_CALL = 2;

    private String tenantId;//所属租户
    private String  appId;//所属应用
    private Date startTime;
    private Date endTime;
    private String fromNum;
    private String toNum;
    private Integer ivrType;
    private String hangupSide;

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

    @Column(name = "start_time")
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Column(name = "end_time")
    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Column(name = "from_num")
    public String getFromNum() {
        return fromNum;
    }

    public void setFromNum(String fromNum) {
        this.fromNum = fromNum;
    }

    @Column(name = "to_num")
    public String getToNum() {
        return toNum;
    }

    public void setToNum(String toNum) {
        this.toNum = toNum;
    }

    @Column(name = "ivr_type")
    public Integer getIvrType() {
        return ivrType;
    }

    public void setIvrType(Integer ivrType) {
        this.ivrType = ivrType;
    }

    @Column(name = "hangup_side")
    public String getHangupSide() {
        return hangupSide;
    }

    public void setHangupSide(String hangupSide) {
        this.hangupSide = hangupSide;
    }
}
