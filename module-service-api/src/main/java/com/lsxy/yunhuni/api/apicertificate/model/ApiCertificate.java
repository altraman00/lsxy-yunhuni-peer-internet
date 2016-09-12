package com.lsxy.yunhuni.api.apicertificate.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * api凭证
 * Created by liups on 2016/6/29.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_api_cert")
public class ApiCertificate extends IdEntity {
    private Tenant tenant;      //租户
    private String certId;      //凭证ID
    private String secretKey;   //凭证密钥

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "cert_id")
    public String getCertId() {
        return certId;
    }

    public void setCertId(String certId) {
        this.certId = certId;
    }

    @Column(name = "secret_key")
    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }
}
