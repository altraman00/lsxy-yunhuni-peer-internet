package com.lsxy.yuhuni.app.model;

import com.lsxy.framework.core.persistence.IdEntity;
import com.lsxy.framework.tenant.model.Tenant;

import javax.persistence.*;

/**
 * 应用
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_app")
public class App extends IdEntity {
    private Tenant tenant;
    private String name;
    private int status;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
