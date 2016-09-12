package com.lsxy.yunhuni.api.resourceTelenum.model;

import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 全局号码资源
 * Created by zhangxb on 2016/7/1.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema = "db_lsxy_bi_yunhuni",name="tb_oc_resource_telenum")
public class ResourceTelenum extends IdEntity{
    public static final int STATUS_RENTED = 1; //已被租用
    public static final int STATUS_FREE = 0;    //未被租用

    private Integer status;//1:已被租用 0:未被租用
    private String telNumber;//号码
    private Tenant tenant;//所属租户
//    private LineGateway line;  //所属线路
    private String operator; //运营商
    private String provider;    //供应商
    private String remark;      //备注

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
    @Column(name = "tel_number")
    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    @Column(name = "operator")
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Column(name = "provider")
    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
