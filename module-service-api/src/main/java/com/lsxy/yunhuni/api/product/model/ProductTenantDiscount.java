package com.lsxy.yunhuni.api.product.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 租户产品计费项折扣表
 * Created by liups on 2016/8/27.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_product_tenant_discount")
public class ProductTenantDiscount  extends IdEntity {
    private String tenantId;
    private String productId;   //产品计费项Id（不是对产品，是对产品计费项）
    private Double discount;    //小数点后两位数字
    private String remark;

    public ProductTenantDiscount() {
    }

    public ProductTenantDiscount(String tenantId, String productId, Double discount) {
        this.tenantId = tenantId;
        this.productId = productId;
        this.discount = discount;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "product_id")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Column(name = "discount")
    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
