package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/7/22.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_bill_day")
public class BillDay extends IdEntity {
    private Date dt;                    //统计时间
    private Integer day;              //天
    private String type;                //统计项目
    private BigDecimal amount;              //总额
    private String appId;               //所属应用
    private String tenantId;            //所属租户

    public BillDay() {
    }

    /**
     * 用于Hql查询，别删
     */
    public BillDay(String tenantId, Date dt, String type, BigDecimal amount){
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

    @Column(name = "day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
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
