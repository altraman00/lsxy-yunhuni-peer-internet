package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;

import javax.persistence.*;

/**
 * 全局号码资源
 * Created by zhangxb on 2016/7/1.
 */
@Entity
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_bi_yy_resource_telenum")
public class ResourceTelenum extends IdEntity{
    public static final int STATUS_RENTED = 1;
    public static final int STATUS_FREE = 0;

    private Integer status;//1:已被租用 0:未被租用
    private String telNumber;//号码
    private Tenant tenant;//所属租户

    @OneToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }
}
