package com.lsxy.yunhuni.api.recharge.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yunhuni.api.recharge.enums.RechargeType;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值记录表
 * Created by liups on 2016/7/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_base", name = "tb_base_recharge")
public class Recharge  extends IdEntity {

    private Tenant tenant;                //所属租户
    private BigDecimal amount;                //充值金额
    private String type;                  //充值方式 参考RechargeType
    private String status;                //充值状态 参考RechargeStatus
    private String orderId;               //订单ID
    private Date payTime;                   //付款时间,人工充值的付款时间为创建时的时间

    public Recharge() {
    }

    public Recharge(Tenant tenant, BigDecimal amount, RechargeType type, RechargeStatus status, String orderId,Date payTime) {
        this.tenant = tenant;
        this.amount = amount;
        this.type = type.name();
        this.status = status.name();
        this.orderId = orderId;
        this.payTime = payTime;
    }

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Column(name = "order_id")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "pay_time")
    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
}
