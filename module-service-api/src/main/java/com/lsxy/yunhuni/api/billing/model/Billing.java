package com.lsxy.yunhuni.api.billing.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 账务
 * Created by liups on 2016/6/27.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_billing")
public class Billing extends IdEntity {

    private Tenant tenant;                //所属租户
    private BigDecimal balance;               //余额
    private Long voiceRemain;          //剩余语音流量（秒）
    private Long smsRemain;            //语音短信余量（条）
    private Long conferenceRemain;     //会议剩余量（秒）
    private Long fileRemainSize;//剩余存储容量，单位byte
    private Long fileTotalSize;//总容量，单位byte

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

    public Long getFileTotalSize() {
        return fileTotalSize;
    }

    public void setFileTotalSize(Long fileTotalSize) {
        this.fileTotalSize = fileTotalSize;
    }
}
