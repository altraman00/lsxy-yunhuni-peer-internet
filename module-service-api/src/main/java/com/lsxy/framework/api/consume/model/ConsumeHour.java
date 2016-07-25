package com.lsxy.framework.api.consume.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 消费小时统计
 * Created by zhangxb on 2016/7/6.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_consume_hour")
public class ConsumeHour extends IdEntity {
    private Date dt;//统计时间
    private Integer hour;//统计小时 区间0-23
    private BigDecimal amongAmount;//本小时消费统计 decimal(20,4) DEFAULT NULL,
    private BigDecimal sumAmount;//累计消费金额,
    private Integer amongSessionConut;//本小时会话数统计
    private Integer sumSessionCount;//累计会话统计
    private String appId;//所属应用,
    private String tenantId;//所属租户

    @Column(name = "hour")
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
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
