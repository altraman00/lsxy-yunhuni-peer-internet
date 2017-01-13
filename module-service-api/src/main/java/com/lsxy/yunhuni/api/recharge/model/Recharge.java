package com.lsxy.yunhuni.api.recharge.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.recharge.enums.RechargeSource;
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

    private String tenantId;                //所属租户
    private BigDecimal amount;                //充值金额
    private String source;                  //充值类型（用户充值，手工等各个类型） 参考RechargeSource
    private String type;                  //充值渠道（支付宝,银联，人工等） 参考RechargeType
    private String status;                //充值状态 参考RechargeStatus
    private String orderId;               //订单ID
    private Date payTime;                   //付款时间,人工充值的付款时间为创建时的时间
    private Date deadline;//过期时间

    public Recharge() {
    }

    public Recharge(String tenantId, BigDecimal amount, RechargeSource source, RechargeType type, RechargeStatus status, String orderId, Date payTime) {
        this.tenantId = tenantId;
        this.amount = amount;
        this.source = source.name();
        this.type = type.name();
        this.status = status.name();
        this.orderId = orderId;
        this.payTime = payTime;
    }
    public Recharge(String tenantId, BigDecimal amount, RechargeSource source, RechargeType type, RechargeStatus status, String orderId, Date payTime,Date deadline) {
        this.tenantId = tenantId;
        this.amount = amount;
        this.source = source.name();
        this.type = type.name();
        this.status = status.name();
        this.orderId = orderId;
        this.payTime = payTime;
        this.deadline = deadline;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
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

    @Column(name = "source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Column(name = "deadline")
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }
}
