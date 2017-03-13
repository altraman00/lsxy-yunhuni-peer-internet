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
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_send_record")
public class MsgSendRecord extends IdEntity {
    public static final int STATE_WAIT = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = -1;

    private String msgKey;
    private String tenantId;
    private String appId;
    private String subaccountId;
    private String taskId;
    private String taskName;
    private String sendType;
    private String supplierCode;
    private String operator;
    private String msg;
    private String tempId;
    private String supplierTempId;
    private String tempArgs;
    private Date sendTime;
    private BigDecimal msgCost;
    private Boolean isMass;
    private Long sumNum;
    private Integer state;
    private Long succNum;
    private Long failNum;
    private Long pendingNum;
    private Integer sendFailTime;
    private String reason;
    private String remark;

    public MsgSendRecord(String msgKey) {
        this.msgKey = msgKey;
    }

    public MsgSendRecord(String msgKey, String tenantId, String appId, String subaccountId, String taskId, String sendType, String supplierCode, String operator, String msg,
                         String tempId, String supplierTempId, String tempArgs, Date sendTime, BigDecimal msgCost) {
        this(null,msgKey,tenantId,appId,subaccountId,taskId,null,sendType,supplierCode,operator,msg,tempId,supplierTempId,tempArgs,sendTime,msgCost,false,1L,1L,0L,STATE_WAIT);

    }

    public MsgSendRecord(String id,String msgKey, String tenantId, String appId, String subaccountId, String taskId, String taskName, String sendType, String supplierCode, String operator,
                         String msg, String tempId, String supplierTempId, String tempArgs, Date sendTime, BigDecimal msgCost, Boolean isMass, Long sumNum, Long pendingNum,Long failNum,Integer state) {
        this.id = id;
        this.msgKey = msgKey;
        this.tenantId = tenantId;
        this.appId = appId;
        this.subaccountId = subaccountId;
        this.taskId = taskId;
        this.taskName = taskName;
        this.sendType = sendType;
        this.supplierCode = supplierCode;
        this.operator = operator;
        this.msg = msg;
        this.tempId = tempId;
        this.supplierTempId = supplierTempId;
        this.tempArgs = tempArgs;
        this.sendTime = sendTime;
        this.msgCost = msgCost;
        this.isMass = isMass;
        this.sumNum = sumNum;
        this.pendingNum = pendingNum;
        this.failNum = failNum;
        this.state = state;
    }

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

    @Column(name = "task_name")
    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    @Column(name = "send_type")
    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    @Column(name = "supplier_code")
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
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

    @Column(name = "is_mass")
    public Boolean getIsMass() {
        return isMass;
    }

    public void setIsMass(Boolean mass) {
        isMass = mass;
    }

    @Column(name = "sum_num")
    public Long getSumNum() {
        return sumNum;
    }

    public void setSumNum(Long sumNum) {
        this.sumNum = sumNum;
    }

    @Column(name = "state")
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @Column(name = "succ_num")
    public Long getSuccNum() {
        return succNum;
    }

    public void setSuccNum(Long succNum) {
        this.succNum = succNum;
    }

    @Column(name = "fail_num")
    public Long getFailNum() {
        return failNum;
    }

    public void setFailNum(Long failNum) {
        this.failNum = failNum;
    }

    @Column(name = "send_fail_time")
    public Integer getSendFailTime() {
        return sendFailTime;
    }

    public void setSendFailTime(Integer sendFailTime) {
        this.sendFailTime = sendFailTime;
    }

    @Column(name = "pending_num")
    public Long getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Long pendingNum) {
        this.pendingNum = pendingNum;
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
