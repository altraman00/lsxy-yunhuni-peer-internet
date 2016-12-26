package com.lsxy.app.oc.rest.config.vo;

/**
 * Created by zhangxb on 2016/11/28.
 */
public class ProductTenantDiscountVo {
    private String productItemId;//计费项id
    private String productItemName;//服务项目
    private String price;//基础价
    private String discount;//折扣

    public ProductTenantDiscountVo() {
    }

    public ProductTenantDiscountVo(String productItemId,String productItemName, String price, String discount) {
        this.productItemId = productItemId;
        this.productItemName = productItemName;
        this.price = price;
        this.discount = discount;
    }

    public String getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(String productItemId) {
        this.productItemId = productItemId;
    }

    public String getProductItemName() {
        return productItemName;
    }

    public void setProductItemName(String productItemName) {
        this.productItemName = productItemName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
