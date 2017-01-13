package com.lsxy.yunhuni.api.apicertificate.model;


import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * 鉴权账号（凭证）变更记录
 * Created by liups on 2016/6/29.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_cert_changelog")
public class ApiCertificateChangeLog extends IdEntity {
    private String certId;  //对应的鉴权账号Id（凭证）
    private Date changeDate;                //更改的时间
    private String secretKey;               //更改后的secretKey
    private String changeType;              //更改的类型


    public ApiCertificateChangeLog(){

    }

    public ApiCertificateChangeLog(String certId, String secretKey, String changeType) {
        this.certId = certId;
        this.secretKey = secretKey;
        this.changeType = changeType;
        this.changeDate = new Date();
    }

    @Column(name = "cert_id")
    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
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
