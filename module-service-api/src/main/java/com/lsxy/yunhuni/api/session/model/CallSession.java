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
    public static final Integer TYPE_VOICE_CALL = 1;
    public static final Integer TYPE_VOICE_CALLBACK = 2;
    public static final Integer TYPE_VOICE_MEETING = 3;
    public static final Integer TYPE_VOICE_IVR = 4;
    public static final Integer TYPE_VOICE_VOICECODE = 5;
    public static final Integer TYPE_VOICE_RECORDING = 6;

    private Integer status;         //状态
    private App app;            //所属APP
    private Tenant tenant;      //所属tenant
    private String relevanceId;//关联标识
    private Integer type;//会话类型1.语音呼叫 2.双向回拨 3.会议 4.IVR定制服务  5.语音验证码 6.录音
    @Column(name = "relevance_id")
    public String getRelevanceId() {
        return relevanceId;
    }

    public void setRelevanceId(String relevanceId) {
        this.relevanceId = relevanceId;
    }
    @Column(name = "type")
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
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
