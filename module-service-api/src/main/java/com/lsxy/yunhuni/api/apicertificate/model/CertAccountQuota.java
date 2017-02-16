package com.lsxy.yunhuni.api.apicertificate.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.core.utils.DateUtils;
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
    public static int CALTYPE_SUM = 1;
    public static int CALTYPE_COUNT = 2;
    private String tenantId;
    private String appId;
    private String certAccountId;
    private String type;  // 配额类型 参考CertAccountQuotaType
    private Integer calType; // 配额计算类型：1，按总数；2，按次数
    private Long period; //默认-1，表示周期无限长
    private Long sum;   //默认-1，表示无限制
    private Long used;
    private Date balanceDt;
    private String remark;
    @Transient
    private String name;

    public CertAccountQuota() {
    }

    /**
     * 仅限新建配额
     */
    public CertAccountQuota(String tenantId, String appId, String certAccountId, String type, Long sum, String remark) {
        new CertAccountQuota(tenantId,appId,certAccountId,type,-1L,sum,remark);
    }

    /**
     * 仅限新建配额
     */
    public CertAccountQuota(String tenantId, String appId, String certAccountId, String type, Long period, Long sum, String remark) {
        try{
            CertAccountQuotaType quotaType = CertAccountQuotaType.valueOf(type);
            this.calType = quotaType.getCalType();
        }catch (Exception e){
            throw new RuntimeException("参数错误");
        }
        this.tenantId = tenantId;
        this.appId = appId;
        this.certAccountId = certAccountId;
        this.type = type;
        this.period = period;
        this.sum = (sum == null ? -1L : sum);
        this.used = 0L;
        this.balanceDt = DateUtils.getPreDate(new Date());
        this.remark = remark;
    }

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
