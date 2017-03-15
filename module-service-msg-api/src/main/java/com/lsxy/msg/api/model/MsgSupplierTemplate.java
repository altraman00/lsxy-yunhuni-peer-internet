package com.lsxy.msg.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liups on 2017/3/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni", name = "tb_bi_msg_supplier_template")
public class MsgSupplierTemplate extends IdEntity {
    public static final int STATUS_PASS = 1;
    public static final int STATUS_WAIT = 0;
    public static final int STATUS_FAIL = -1;
    private String tempId;
    private String tenantId;
    private String appId;
    private String subaccountId;
    private String supplierCode;
    private String supplierTempId;
    private Integer status;
    private String remark;
    private String lastUserName;

    public MsgSupplierTemplate() {
    }

    public MsgSupplierTemplate(String tempId, String tenantId, String appId, String subaccountId, String supplierCode, String supplierTempId,String lastUserName) {
        this.tempId = tempId;
        this.tenantId = tenantId;
        this.appId = appId;
        this.subaccountId = subaccountId;
        this.supplierCode = supplierCode;
        this.supplierTempId = supplierTempId;
        this.status = STATUS_PASS;
        this.lastUserName = lastUserName;
    }

    @Column(name = "temp_id")
    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
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

    @Column(name = "subaccount_id")
    public String getSubaccountId() {
        return subaccountId;
    }

    public void setSubaccountId(String subaccountId) {
        this.subaccountId = subaccountId;
    }

    @Column(name = "supplier_code")
    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    @Column(name = "supplier_temp_id")
    public String getSupplierTempId() {
        return supplierTempId;
    }

    public void setSupplierTempId(String supplierTempId) {
        this.supplierTempId = supplierTempId;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Column(name = "last_user_name")
    public String getLastUserName() {
        return lastUserName;
    }

    public void setLastUserName(String lastUserName) {
        this.lastUserName = lastUserName;
    }

}
