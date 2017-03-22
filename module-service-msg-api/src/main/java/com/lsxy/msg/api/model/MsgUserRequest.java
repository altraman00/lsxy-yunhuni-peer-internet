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
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_user_request")
public class MsgUserRequest extends IdEntity {
    public static final int STATE_WAIT = 0;
    public static final int STATE_SUCCESS = 1;
    public static final int STATE_FAIL = -1;

    private String msgKey;
    private String tenantId;
    private String appId;
    private String subaccountId;
    private String taskName;
    private String sendType;
    private String mobile;
    private String mobiles;
    private String msg;
    private String tempId;
    private String tempArgs;
    private Date sendTime;
    private BigDecimal msgCost;
    private Boolean isMass;
    private Long sumNum;
    private Integer state;
    private Long succNum;
    private Long failNum;
    private Long pendingNum;
    private Long invalidNum;

    private String reason;
    private String remark;

    public MsgUserRequest() {
    }


    public MsgUserRequest(String msgKey, String tenantId, String appId, String subaccountId, String sendType,String mobile, String msg, String tempId,
                          String tempArgs, Date sendTime,BigDecimal msgCost,Integer state,Date createTime) {
        this(msgKey, tenantId, appId, subaccountId,null, sendType, mobile,null, msg, tempId, tempArgs, sendTime, msgCost,false,1L,state,null,null,null,createTime);
        if(STATE_WAIT == state){
            this.pendingNum = 1L;
        }
        if(STATE_FAIL == state){
            this.failNum = 1L;
        }
    }

    public MsgUserRequest(String msgKey, String tenantId, String appId, String subaccountId, String taskName, String sendType, String mobile, String mobiles, String msg, String tempId,
                          String tempArgs, Date sendTime, BigDecimal msgCost, Boolean isMass, Long sumNum, Integer state, Long pendingNum, Long invalidNum, String reason,Date createTime) {
        this.msgKey = msgKey;
        this.tenantId = tenantId;
        this.appId = appId;
        this.subaccountId = subaccountId;
        this.taskName = taskName;
        this.sendType = sendType;
        this.mobile = mobile;
        this.mobiles = mobiles;
        this.msg = msg;
        this.tempId = tempId;
        this.tempArgs = tempArgs;
        this.sendTime = sendTime;
        this.msgCost = msgCost;
        this.isMass = isMass;
        this.sumNum = sumNum;
        this.state = state;
        this.pendingNum = pendingNum;
        this.invalidNum = invalidNum;
        this.reason = reason;
        this.createTime = createTime;
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

    @Column(name="mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Column(name="mobiles")
    public String getMobiles() {
        return mobiles;
    }

    public void setMobiles(String mobiles) {
        this.mobiles = mobiles;
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

    @Column(name = "pending_num")
    public Long getPendingNum() {
        return pendingNum;
    }

    public void setPendingNum(Long pendingNum) {
        this.pendingNum = pendingNum;
    }

    @Column(name = "invalid_num")
    public Long getInvalidNum() {
        return invalidNum;
    }

    public void setInvalidNum(Long invalidNum) {
        this.invalidNum = invalidNum;
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
