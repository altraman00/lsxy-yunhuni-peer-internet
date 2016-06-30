package com.lsxy.yuhuni.api.apicertificate.model;

import com.lsxy.framework.core.persistence.IdEntity;

import javax.persistence.*;
import java.sql.Date;

/**
 * 鉴权账号（凭证）变更记录
 * Created by liups on 2016/6/29.
 */
@Entity
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_cert_changelog")
public class ApiCertificateChangeLog extends IdEntity {
    private ApiCertificate apiCertificate;  //对应的鉴权账号（凭证）
    private Date changeDate;                //更改的时间
    private String secretKey;               //更改后的secretKey
    private String changeType;              //更改的类型


    public ApiCertificateChangeLog(){

    }

    public ApiCertificateChangeLog(ApiCertificate apiCertificate, String secretKey, String changeType) {
        this.apiCertificate = apiCertificate;
        this.secretKey = secretKey;
        this.changeType = changeType;
        this.changeDate = new Date(System.currentTimeMillis());
    }

    @ManyToOne
    @JoinColumn(name = "cert_id")
    public ApiCertificate getApiCertificate() {
        return apiCertificate;
    }

    public void setApiCertificate(ApiCertificate apiCertificate) {
        this.apiCertificate = apiCertificate;
    }

    @Column(name = "changedt")
    public Date getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(Date changeDate) {
        this.changeDate = changeDate;
    }

    @Column(name = "sk")
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    @Column(name = "changetype")
    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }
}
