package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消费日统计
 * Created by zhangxb on 2016/7/6.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_consume_days")
public class ConsumeDay extends IdEntity{
    private Date dt;//统计时间
    private int day;//统计日 区间1-31
    private double amongAmount;//消费统计 decimal(10,0) DEFAULT NULL,
    private double sumAmount;//消费金额,
    private int amongSessionConut;//会话数统计
    private int sumSessionCount;//会话统计
    private String appId;//所属应用,
    private String tenantId;//所属租户

    @Column(name = "day")
    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Column(name = "sum_amount")
    public double getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(double sumAmount) {
        this.sumAmount = sumAmount;
    }
    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }
    @Column(name = "among_amount")
    public double getAmongAmount() {
        return amongAmount;
    }

    public void setAmongAmount(double amongAmount) {
        this.amongAmount = amongAmount;
    }
    @Column(name = "among_session_count")
    public int getAmongSessionConut() {
        return amongSessionConut;
    }

    public void setAmongSessionConut(int amongSessionConut) {
        this.amongSessionConut = amongSessionConut;
    }
    @Column(name = "sum_session_count")
    public int getSumSessionCount() {
        return sumSessionCount;
    }

    public void setSumSessionCount(int sumSessionCount) {
        this.sumSessionCount = sumSessionCount;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
