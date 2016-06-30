package com.lsxy.yuhuni.api.session.model;

import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.yuhuni.api.app.model.App;

import javax.persistence.*;

/**
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_session")
public class Session extends IdEntity {
    public static final int STATUS_CALLING = 1;
    public static final int STATUS_OVER = 2;
    public static final int STATUS_RINGING = 3;

    private int status;         //状态
    private App app;            //所属APP
    private Tenant tenant;      //所属tenant

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
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
