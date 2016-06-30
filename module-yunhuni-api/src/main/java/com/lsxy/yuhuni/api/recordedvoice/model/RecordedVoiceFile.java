package com.lsxy.yuhuni.api.recordedvoice.model;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.yuhuni.api.session.model.Session;
import com.lsxy.yuhuni.api.app.model.App;

import javax.persistence.*;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_recored_voice_file")
public class RecordedVoiceFile extends IdEntity {
    private String url;     //文件URL
    private App app;        //所属APP
    private double size;    //文件大小
    private Session session; //所属会话

    @Column(name = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @ManyToOne
    @JoinColumn(name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @Column(name = "size")
    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    @OneToOne
    @JoinColumn(name = "session_id")
    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
