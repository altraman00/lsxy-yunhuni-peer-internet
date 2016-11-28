package com.lsxy.yunhuni.api.product.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * 产品子项目
 * Created by zhangxb on 2016/8/27.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_product_item")
public class ProductItem extends IdEntity {
    private Product product;//所属产品类别
    private String name;    //产品名称
    private String code;    //一次写入，不可修改，涉及到编程
    private Integer calType;    //1、按数量，2、按时长
    private String remark;
    private Integer status;//是否可用1是0否
    @Column(name = "cal_type")
    public Integer getCalType() {
        return calType;
    }

    public void setCalType(Integer calType) {
        this.calType = calType;
    }
    @OneToOne
    @JoinColumn(name = "product_id")
    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "code")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
