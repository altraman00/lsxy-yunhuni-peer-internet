package com.lsxy.framework.tenant.model;

import com.lsxy.framework.core.persistence.IdEntity;

import javax.persistence.*;

/**
 * Created by liups on 2016/6/27.
 */
@Entity
@Table(schema="db_lsxy_base",name = "tb_base_billing")
public class Billing extends IdEntity {

    private Tenant tenant;                //所属租户
    private Double balance;               //余额
    private Integer voiceRemain;          //剩余语音流量
    private Integer smsRemain;            //语音短信余量

    @OneToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "balance")
    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Column(name = "voice_remain")
    public Integer getVoiceRemain() {
        return voiceRemain;
    }

    public void setVoiceRemain(Integer voiceRemain) {
        this.voiceRemain = voiceRemain;
    }

    @Column(name = "sms_remain")
    public Integer getSmsRemain() {
        return smsRemain;
    }

    public void setSmsRemain(Integer smsRemain) {
        this.smsRemain = smsRemain;
    }
}
