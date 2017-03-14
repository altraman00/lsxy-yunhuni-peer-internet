package com.lsxy.framework.api.tenant.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 租户功能开关表
 * Created by liups on 2016/6/29.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_base",name = "tb_base_tenant_service")
public class TenantServiceSwitch extends IdEntity {

    private Tenant tenant;//所属租户
    private Integer isVoiceDirectly;//是否语音直拨 0否，1是
    private Integer isVoiceCallback;//是否语音回拨0否，1是
    private Integer isSessionService;//是否会议服务0否，1是
    private Integer isRecording;//是否录音服务0否，1是
    private Integer isVoiceValidate;//是否语音验证码0否，1是
    private Integer isIvrService;//是否IVR定制服务0否，1是
    private Integer isCallCenter;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "is_voice_directly")
    public Integer getIsVoiceDirectly() {
        return isVoiceDirectly;
    }

    public void setIsVoiceDirectly(Integer isVoiceDirectly) {
        this.isVoiceDirectly = isVoiceDirectly;
    }
    @Column(name = "is_voice_callback")
    public Integer getIsVoiceCallback() {
        return isVoiceCallback;
    }

    public void setIsVoiceCallback(Integer isVoiceCallback) {
        this.isVoiceCallback = isVoiceCallback;
    }

    @Column(name = "is_session_service")
    public Integer getIsSessionService() {
        return isSessionService;
    }

    public void setIsSessionService(Integer isSessionService) {
        this.isSessionService = isSessionService;
    }
    @Column(name = "is_recording")
    public Integer getIsRecording() {
        return isRecording;
    }

    public void setIsRecording(Integer isRecording) {
        this.isRecording = isRecording;
    }
    @Column(name = "is_voice_validate")
    public Integer getIsVoiceValidate() {
        return isVoiceValidate;
    }

    public void setIsVoiceValidate(Integer isVoiceValidate) {
        this.isVoiceValidate = isVoiceValidate;
    }
    @Column(name = "is_ivr_service")
    public Integer getIsIvrService() {
        return isIvrService;
    }

    public void setIsIvrService(Integer isIvrService) {
        this.isIvrService = isIvrService;
    }

    @Column(name = "is_call_center")
    public Integer getIsCallCenter() {
        return isCallCenter;
    }

    public void setIsCallCenter(Integer isCallCenter) {
        this.isCallCenter = isCallCenter;
    }
}
