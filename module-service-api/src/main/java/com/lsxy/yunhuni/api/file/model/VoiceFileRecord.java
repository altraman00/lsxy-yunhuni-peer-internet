package com.lsxy.yunhuni.api.file.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;

import javax.persistence.*;

/**
 * 录音文件
 * Created by zhangxb on 2016/7/21.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_voice_file_record")
public class VoiceFileRecord extends IdEntity {
    private Tenant tenant;//所属租户
    private App app;//所属应用
    private CallSession session;//所属会话
    private String name;//文件名 生成规则yyyyMMddhhmmss-[sessionid]
    private String url;//录音文件URL
    private Long size;//文件大小
    private Long duration;//时长
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }
    @Column(name="url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    @Column(name="size")
    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    @Column(name="name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @OneToOne
    @JoinColumn(name = "session_id")
    public CallSession getSession() {
        return session;
    }

    public void setSession(CallSession session) {
        this.session = session;
    }
    @Column(name="duration")
    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
