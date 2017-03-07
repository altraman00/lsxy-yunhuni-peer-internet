package com.lsxy.msg.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2017/3/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_send_detail")
public class MsgSendDetail extends IdEntity {
    private String msgKey;
    private String tenantId;
    private String appId;
    private String subaccountId;
    private String taskId;
    private String mobile;
    private String msg;
    private String tempId;
    private String supplierTempId;
    private String tempArgs;
    private Date sendTime;
    private BigDecimal msgCost;
    private Integer sendType;
    private String supplierId;
    private String operator;
    private Integer state;
    private String reason;
    private String remark;

    @Column(name = "msg_key")
    public String getMsgKey() {
        return msgKey;
    }

    public void setMsgKey(String msgKey) {
        this.msgKey = msgKey;
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

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "task_id")
    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Column(name = "temp_id")
    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    @Column(name = "supplier_temp_id")
    public String getSupplierTempId() {
        return supplierTempId;
    }

    public void setSupplierTempId(String supplierTempId) {
        this.supplierTempId = supplierTempId;
    }

    @Column(name = "temp_args")
    public String getTempArgs() {
        return tempArgs;
    }

    public void setTempArgs(String tempArgs) {
        this.tempArgs = tempArgs;
    }

    @Column(name = "send_time")
    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    @Column(name = "msg_cost")
    public BigDecimal getMsgCost() {
        return msgCost;
    }

    public void setMsgCost(BigDecimal msgCost) {
        this.msgCost = msgCost;
    }

    @Column(name = "send_type")
    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    @Column(name = "supplier_id")
    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "reason")
    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
