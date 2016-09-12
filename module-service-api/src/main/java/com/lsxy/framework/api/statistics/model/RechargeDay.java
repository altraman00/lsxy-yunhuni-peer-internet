package com.lsxy.framework.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单记录日统计
 * Created by zhangxb on 2016/8/5.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_base",name = "tb_base_recharge_day")
public class RechargeDay extends IdEntity {
    private String tenantId;//所属租户
    private Date dt;//统计时间
    private Integer day;//统计日期范围1-31
    private BigDecimal among_amount;//该时间充值金额
    private Long among_num;//该时间充值次数
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    @Column(name = "among_amount")
    public BigDecimal getAmong_amount() {
        return among_amount;
    }

    public void setAmong_amount(BigDecimal among_amount) {
        this.among_amount = among_amount;
    }
    @Column(name = "among_num")
    public Long getAmong_num() {
        return among_num;
    }

    public void setAmong_num(Long among_num) {
        this.among_num = among_num;
    }
}
