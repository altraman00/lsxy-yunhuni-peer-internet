package com.lsxy.app.api.gateway.dto;

import java.util.List;

/**
 * Created by liuws on 2017/2/16.
 */
public class ApiCertificateSubAccountOutput extends CommonDTO {

    private String appId;
    private String parentId;
    private String callbackUrl;
    private Integer enabled;     //状态：1，可用0，不可用
    private String remark;
    private String certId;      //凭证ID
    private String secretKey;   //凭证密钥
    private List<QuotaDTO> quotas;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getEnabled() {
        return enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public List<QuotaDTO> getQuotas() {
        return quotas;
    }

    public void setQuotas(List<QuotaDTO> quotas) {
        this.quotas = quotas;
    }
}
