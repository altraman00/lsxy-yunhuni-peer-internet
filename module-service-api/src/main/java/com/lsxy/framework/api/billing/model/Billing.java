package com.lsxy.framework.api.billing.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 账务
 * Created by liups on 2016/6/27.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_base",name = "tb_base_billing")
public class Billing extends IdEntity {

    private Tenant tenant;                //所属租户
    private BigDecimal balance;               //余额
    private Long voiceRemain;          //剩余语音流量（秒）
    private Long smsRemain;            //语音短信余量（条）
    private Long conferenceRemain;     //会议剩余量（秒）
    private Long fileRemainSize;//剩余存储容量，单位byte
    private Long fileTotalSize;//总容量，单位byte
    private Date balanceDate;   //结算时间
    private BigDecimal addBalance;    //总充值额
    private BigDecimal usebalance;    //总消费额
    private Long callConnect;    //总接通量
    private Long callNotConnect;    //总不接通量
    private Long callCostTime;    //总通话时长

    @Column(name = "file_remain_size")
    public Long getFileRemainSize() {
        return fileRemainSize;
    }
    public void setFileRemainSize(Long fileRemainSize) {
        this.fileRemainSize = fileRemainSize;
    }
    @OneToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "balance")
    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Column(name = "voice_remain")
    public Long getVoiceRemain() {
        return voiceRemain;
    }

    public void setVoiceRemain(Long voiceRemain) {
        this.voiceRemain = voiceRemain;
    }

    @Column(name = "sms_remain")
    public Long getSmsRemain() {
        return smsRemain;
    }

    public void setSmsRemain(Long smsRemain) {
        this.smsRemain = smsRemain;
    }

    @Column(name = "conference_remain")
    public Long getConferenceRemain() {
        return conferenceRemain;
    }

    public void setConferenceRemain(Long conferenceRemain) {
        this.conferenceRemain = conferenceRemain;
    }

    @Column(name = "file_total_size")
    public Long getFileTotalSize() {
        return fileTotalSize;
    }

    public void setFileTotalSize(Long fileTotalSize) {
        this.fileTotalSize = fileTotalSize;
    }

    @Column(name = "balance_date")
    public Date getBalanceDate() {
        return balanceDate;
    }

    public void setBalanceDate(Date balanceDate) {
        this.balanceDate = balanceDate;
    }

    @Column(name = "add_balance")
    public BigDecimal getAddBalance() {
        return addBalance;
    }

    public void setAddBalance(BigDecimal addBalance) {
        this.addBalance = addBalance;
    }

    @Column(name = "use_balance")
    public BigDecimal getUsebalance() {
        return usebalance;
    }

    public void setUsebalance(BigDecimal usebalance) {
        this.usebalance = usebalance;
    }

    @Column(name = "call_connect")
    public Long getCallConnect() {
        return callConnect;
    }

    public void setCallConnect(Long callConnect) {
        this.callConnect = callConnect;
    }

    @Column(name = "call_not_connect")
    public Long getCallNotConnect() {
        return callNotConnect;
    }

    public void setCallNotConnect(Long callNotConnect) {
        this.callNotConnect = callNotConnect;
    }

    @Column(name = "call_cost_time")
    public Long getCallCostTime() {
        return callCostTime;
    }

    public void setCallCostTime(Long callCostTime) {
        this.callCostTime = callCostTime;
    }
}
