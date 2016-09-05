package com.lsxy.yunhuni.api.session.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;

import javax.persistence.*;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_session")
public class CallSession extends IdEntity {
    public static final int STATUS_CALLING = 1;
    public static final int STATUS_OVER = 2;
    public static final int STATUS_RINGING = 3;
    //TODO 换成与ProductCode枚举关联
    public static final String TYPE_VOICE_CALL = "voice_call";
    public static final String TYPE_VOICE_CALLBACK = "duo_call";
    public static final String TYPE_VOICE_MEETING = "conf_call";
    public static final String TYPE_VOICE_IVR = "ivr_call";
    public static final String TYPE_VOICE_VOICECODE = "captcha_call";
    public static final String TYPE_VOICE_RECORDING = "voice_recording";

    private Integer status;         //状态
    private App app;            //所属APP
    private Tenant tenant;      //所属tenant
    private String relevanceId;//关联标识
    private String type;//查看产品表code字段或枚举类ProductCode
    @Column(name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
