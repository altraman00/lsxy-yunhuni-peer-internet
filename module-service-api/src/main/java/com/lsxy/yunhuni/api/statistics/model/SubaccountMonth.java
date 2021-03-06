package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2017/2/21.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_cert_subaccount_month")
public class SubaccountMonth extends IdEntity {
    private String appId;
    private String tenantId;
    private String subaccountId;
    private Date dt;
    private Integer month;
    private BigDecimal amongAmount;
    private Long amongDuration;
    private Long amongSms;
    private Long amongUssd;
    private Long voiceUsed;
    private Long msgUsed;
    private Long ussdUsed;
    private Long smsUser;
    private Long voiceQuotaValue;
    private Long msgQuotaValue;
    private Long smsQuotaValue;
    private Long ussdQuotaValue;

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "dt")
    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    @Column(name = "month")
    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    @Column(name = "among_amount")
    public BigDecimal getAmongAmount() {
        return amongAmount;
    }

    public void setAmongAmount(BigDecimal amongAmount) {
        this.amongAmount = amongAmount;
    }

    @Column(name = "among_duration")
    public Long getAmongDuration() {
        return amongDuration;
    }

    public void setAmongDuration(Long amongDuration) {
        this.amongDuration = amongDuration;
    }


    @Column(name = "among_sms")
    public Long getAmongSms() {
        return amongSms;
    }

    public void setAmongSms(Long amongSms) {
        this.amongSms = amongSms;
    }

    @Column(name = "among_ussd")
    public Long getAmongUssd() {
        return amongUssd;
    }

    public void setAmongUssd(Long amongUssd) {
        this.amongUssd = amongUssd;
    }

    @Column(name = "voice_used")
    public Long getVoiceUsed() {
        return voiceUsed;
    }

    public void setVoiceUsed(Long voiceUsed) {
        this.voiceUsed = voiceUsed;
    }
    @Column(name = "ussd_used")
    public Long getUssdUsed() {
        return ussdUsed;
    }

    public void setUssdUsed(Long ussdUsed) {
        this.ussdUsed = ussdUsed;
    }
    @Column(name = "sms_used")
    public Long getSmsUser() {
        return smsUser;
    }

    public void setSmsUser(Long smsUser) {
        this.smsUser = smsUser;
    }
    @Column(name = "msg_used")
    public Long getMsgUsed() {
        return msgUsed;
    }

    public void setMsgUsed(Long msgUsed) {
        this.msgUsed = msgUsed;
    }

    @Column(name = "voice_quota_value")
    public Long getVoiceQuotaValue() {
        return voiceQuotaValue;
    }

    public void setVoiceQuotaValue(Long voiceQuotaValue) {
        this.voiceQuotaValue = voiceQuotaValue;
    }

    @Column(name = "msg_quota_value")
    public Long getMsgQuotaValue() {
        return msgQuotaValue;
    }

    public void setMsgQuotaValue(Long msgQuotaValue) {
        this.msgQuotaValue = msgQuotaValue;
    }
    @Column(name = "sms_quota_value")
    public Long getSmsQuotaValue() {
        return smsQuotaValue;
    }

    public void setSmsQuotaValue(Long smsQuotaValue) {
        this.smsQuotaValue = smsQuotaValue;
    }
    @Column(name = "ussd_quota_value")
    public Long getUssdQuotaValue() {
        return ussdQuotaValue;
    }

    public void setUssdQuotaValue(Long ussdQuotaValue) {
        this.ussdQuotaValue = ussdQuotaValue;
    }
}
