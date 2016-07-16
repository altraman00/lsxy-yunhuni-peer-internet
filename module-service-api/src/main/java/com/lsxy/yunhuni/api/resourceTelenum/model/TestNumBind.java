package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.app.model.App;

import javax.persistence.*;

/**
 * 测试号码绑定
 * Created by zhangxb on 2016/7/2.
 */
@Entity
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_bi_test_num_bind")
public class TestNumBind extends IdEntity {
    private String number;//测试号码
    Tenant tenant;//所属租户
    App app;//所属应用

    @Column( name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
    @ManyToOne
    @JoinColumn( name = "app_id")
    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
    }

    @ManyToOne
    @JoinColumn( name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }
}
