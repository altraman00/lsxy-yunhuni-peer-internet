package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 月结账单
 * Created by liups on 2016/7/11.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_bill_month")
public class BillMonth extends IdEntity {
    private Date dt;                    //统计时间
    private Integer month;              //月份
    private String type;                //统计项目
    private Double amount;              //总额
    private String appId;               //所属应用
    private String tenantId;            //所属租户

    public BillMonth(){

    }

    public BillMonth(String tenantId,Date dt,String type,Double amount){
        this.tenantId = tenantId;
        this.dt = dt;
        this.type = type;
        this.amount = amount;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "amount")
    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
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
