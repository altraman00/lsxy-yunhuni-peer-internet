package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 租户配置文件
 * Created by zhangxb on 2016/11/15.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_yy_config_tenant")
public class TenantConfig extends IdEntity {
    private String tenantId;//所属租户
    private String type;//配置类型
    private String name;//配置名
    private String value;//配置值
    private Date expireDt;//有效期
    private String remark;//备注
    private String enabled;//是否可用
    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
    @Column(name = "type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    @Column(name = "expire_dt")
    public Date getExpireDt() {
        return expireDt;
    }

    public void setExpireDt(Date expireDt) {
        this.expireDt = expireDt;
    }
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Column(name = "enabled")
    public String getEnabled() {
        return enabled;
    }

    public void setEnabled(String enabled) {
        this.enabled = enabled;
    }
}
