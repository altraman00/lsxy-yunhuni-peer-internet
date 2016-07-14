package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费日统计
 * Created by zhangxb on 2016/7/6.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_consume_day")
public class ConsumeDay extends IdEntity{
    private Date dt;//统计时间
    private Integer day;//统计日 区间1-31
    private BigDecimal amongAmount;//消费统计 decimal(20,4) DEFAULT NULL,
    private BigDecimal sumAmount;//消费金额,
    private Integer amongSessionConut;//会话数统计
    private Integer sumSessionCount;//会话统计
    private String appId;//所属应用,
    private String tenantId;//所属租户

    @Column(name = "day")
    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    @Column(name = "sum_amount")
    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
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
    public BigDecimal getAmongAmount() {
        return amongAmount;
    }

    public void setAmongAmount(BigDecimal amongAmount) {
        this.amongAmount = amongAmount;
    }
    @Column(name = "among_session_count")
    public int getAmongSessionConut() {
        return amongSessionConut;
    }

    public void setAmongSessionConut(Integer amongSessionConut) {
        this.amongSessionConut = amongSessionConut;
    }
    @Column(name = "sum_session_count")
    public Integer getSumSessionCount() {
        return sumSessionCount;
    }

    public void setSumSessionCount(Integer sumSessionCount) {
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
