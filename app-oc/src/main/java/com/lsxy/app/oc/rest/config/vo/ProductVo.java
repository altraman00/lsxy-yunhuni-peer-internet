package com.lsxy.app.oc.rest.config.vo;

import com.lsxy.yunhuni.api.product.model.ProductPrice;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zhangxb on 2016/11/26.
 */
public class ProductVo {
    private String id;//
    private Date createTime;//创建时间
    private String priceItem;//计费项
    private String priceKey;//计费标识
    private String productType;//归属产品
    private BigDecimal price;//价格
    private String unit;//计价单位

    public ProductVo(ProductPrice productPrice) {
        this.id = productPrice.getId();
        this.createTime = productPrice.getCreateTime();
        this.priceItem = productPrice.getProductItem().getName();
        this.priceKey = productPrice.getProductItem().getCode();
        this.productType = productPrice.getProductItem().getProduct().getName();
        this.price = productPrice.getPrice();
        this.unit = productPrice.getTimeUnit()+productPrice.getUnit();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(String priceItem) {
        this.priceItem = priceItem;
    }


    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriceKey() {
        return priceKey;
    }

    public void setPriceKey(String priceKey) {
        this.priceKey = priceKey;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
