package com.lsxy.yunhuni.api.apicertificate.model;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import java.util.List;

/**
 * 鉴权子账号
 * Created by liups on 2017/2/14.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_cert_subaccount")
@Where(clause = "deleted=0")
@PrimaryKeyJoinColumn(name = "id")
public class ApiCertificateSubAccount extends ApiCertificate{
    private String appId;
    private String parentId;
    private String callbackUrl;
    private Integer enabled;     //状态：1，可用0，不可用
    private String remark;

    public ApiCertificateSubAccount() {
    }

    public ApiCertificateSubAccount(String appId, String callbackUrl, String remark, List<CertAccountQuota> quotas) {
        this.enabled = 0;//默认不可用
        this.appId = appId;
        this.callbackUrl = callbackUrl;
        this.remark = remark;
        super.setQuotas(quotas);
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "parent_id")
    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Column(name = "callback_url")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    @Column(name = "enabled")
    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }



    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
