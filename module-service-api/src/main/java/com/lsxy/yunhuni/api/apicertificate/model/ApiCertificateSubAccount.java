package com.lsxy.yunhuni.api.apicertificate.model;

import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * Created by liups on 2017/2/14.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_cert_subaccount")
@Where(clause = "deleted=0")
@PrimaryKeyJoinColumn(name = "id")
public class ApiCertificateSubAccount extends ApiCertificate{
    private String appId;
    private String callbackUrl;

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "callback_url")
    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }
}
