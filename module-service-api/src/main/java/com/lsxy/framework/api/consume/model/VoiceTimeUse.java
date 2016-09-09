package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liups on 2016/8/30.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_base",name = "tb_base_voice_time_use")
public class VoiceTimeUse extends IdEntity {
    private Date dt;
    private String type;    //1.语音呼叫2.双向回拨3.会议4.IVR定制服务5.语音验证码6.录音
    private Long voiceTime; //通话时长
    private Long useTime;   //扣量时长
    private Integer unitTime;   //单位时长
    private String unit;        //单位
    private String appId;
    private String tenantId;

    public VoiceTimeUse() {
    }

    public VoiceTimeUse(Date dt, String type, Long voiceTime, Long useTime, Integer unitTime, String unit, String appId, String tenantId) {
        this.dt = dt;
        this.type = type;
        this.voiceTime = voiceTime;
        this.useTime = useTime;
        this.unitTime = unitTime;
        this.unit = unit;
        this.appId = appId;
        this.tenantId = tenantId;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "voice_time")
    public Long getVoiceTime() {
        return voiceTime;
    }

    public void setVoiceTime(Long voiceTime) {
        this.voiceTime = voiceTime;
    }
    @Column(name = "use_time")
    public Long getUseTime() {
        return useTime;
    }

    public void setUseTime(Long useTime) {
        this.useTime = useTime;
    }
    @Column(name = "unit_time")
    public Integer getUnitTime() {
        return unitTime;
    }

    public void setUnitTime(Integer unitTime) {
        this.unitTime = unitTime;
    }
    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
