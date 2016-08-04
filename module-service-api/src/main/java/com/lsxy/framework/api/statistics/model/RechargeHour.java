package com.lsxy.framework.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单记录小时统计
 * Created by zhangxb on 2016/8/1.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_recharge_hour")
public class RechargeHour extends IdEntity {
    private String tenantId;//所属租户
    private Date dt;//统计时间
    private Integer hour;//统计日期范围1-31
    private BigDecimal among_amount;//该时间充值金额
    private BigDecimal sum_amount;//总充值金额
    private Long sum_num;//总充值次数
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

    @Column(name = "hour")
    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }
    @Column(name = "among_amount")
    public BigDecimal getAmong_amount() {
        return among_amount;
    }

    public void setAmong_amount(BigDecimal among_amount) {
        this.among_amount = among_amount;
    }
    @Column(name = "sum_amount")
    public BigDecimal getSum_amount() {
        return sum_amount;
    }

    public void setSum_amount(BigDecimal sum_amount) {
        this.sum_amount = sum_amount;
    }
    @Column(name = "sum_num")
    public Long getSum_num() {
        return sum_num;
    }

    public void setSum_num(Long sum_num) {
        this.sum_num = sum_num;
    }
}
