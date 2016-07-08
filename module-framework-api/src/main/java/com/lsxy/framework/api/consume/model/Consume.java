package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消费记录表对象
 * Created by zhangxb on 2016/7/8.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_consume")
public class Consume extends IdEntity {
    private Date dt;//消费时间
    private String type;//消费类型 目前直接存名字
    private double amount;//消费金额
    private String remark;//备注
    private String appId;//所属应用编号（仅用查询，如需关联应用等合并结构后改）
    private String tenantId;//所属租户编号（仅用查询）
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
    @Column(name = "amount")
    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
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
