package com.lsxy.yunhuni.api.statistics.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by liups on 2017/3/14.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_msg_month_statistics")
public class MsgMonth extends IdEntity {
    private String tenantId;
    private String appId;
    private String subaccountId;
    private String type;
    private Long total;
    private Long success;
    private Long fail;
    private Date dt;
    private Integer month;

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

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Column(name = "total")
    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    @Column(name = "success")
    public Long getSuccess() {
        return success;
    }

    public void setSuccess(Long success) {
        this.success = success;
    }

    @Column(name = "fail")
    public Long getFail() {
        return fail;
    }

    public void setFail(Long fail) {
        this.fail = fail;
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
}
