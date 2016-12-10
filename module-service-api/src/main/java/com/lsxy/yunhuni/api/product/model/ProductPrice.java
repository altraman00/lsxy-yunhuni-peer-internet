package com.lsxy.yunhuni.api.product.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * 产品计费项资费表
 * Created by liups on 2016/8/27.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_config_product_price")
public class ProductPrice extends IdEntity {
    public static int STATUS_VALID = 1;
    public static int STATUS_INVALID = 0;
    private ProductItem productItem;    //产品
    private BigDecimal price;   //单价
    private Integer status;     //1启用，0未启用
    private Integer priority;   //优先级，数字大的优先
    private Long priceVersion;  //资费版本号，同一套资费版本号一样，创建后不可修改，用时间戳
    private String remark;
    private Integer timeUnit;   //单位时长(单位秒)
    private String unit;   //单位
    @Column(name = "time_unit")
    public Integer getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }
    @Column(name = "unit")
    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
    @ManyToOne
    @JoinColumn(name = "product_item_id")
    public ProductItem getProductItem() {
        return productItem;
    }

    public void setProductItem(ProductItem productItem) {
        this.productItem = productItem;
    }
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Column(name = "status")
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Column(name = "priority")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "price_version")
    public Long getPriceVersion() {
        return priceVersion;
    }

    public void setPriceVersion(Long priceVersion) {
        this.priceVersion = priceVersion;
    }

    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
