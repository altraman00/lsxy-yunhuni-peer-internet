package com.lsxy.yunhuni.api.apicertificate.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 鉴权账号配额
 * Created by liups on 2017/2/15.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_cert_account_quota")
public class CertAccountQuota extends IdEntity {
    private String tenantId;
    private String appId;
    private String certAccountId;
    private String type;  // 配额类型 参考CertAccountQuotaType
    private Integer calType; // 配额计算类型：1，按时长；2，按个数
    private Long period; //默认-1，表示周期无限长
    private Long sum;   //默认-1，表示无限制
    private Long used;
    private Date balanceDt;
    private String remark;
    @Transient
    private String name;

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "cert_account_id")
    public String getCertAccountId() {
        return certAccountId;
    }

    public void setCertAccountId(String certAccountId) {
        this.certAccountId = certAccountId;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "cal_type")
    public Integer getCalType() {
        return calType;
    }

    public void setCalType(Integer calType) {
        this.calType = calType;
    }

    @Column(name = "period")
    public Long getPeriod() {
        return period;
    }

    public void setPeriod(Long period) {
        this.period = period;
    }

    @Column(name = "sum")
    public Long getSum() {
        return sum;
    }

    public void setSum(Long sum) {
        this.sum = sum;
    }

    @Column(name = "used")
    public Long getUsed() {
        return used;
    }

    public void setUsed(Long used) {
        this.used = used;
    }

    @Column(name = "balance_dt")
    public Date getBalanceDt() {
        return balanceDt;
    }

    public void setBalanceDt(Date balanceDt) {
        this.balanceDt = balanceDt;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Transient
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
