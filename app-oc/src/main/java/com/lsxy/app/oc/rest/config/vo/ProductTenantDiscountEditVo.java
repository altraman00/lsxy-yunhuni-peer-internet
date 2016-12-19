package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/11/28.
 */
@ApiModel
public class ProductTenantDiscountEditVo {
    @ApiModelProperty(name="productItemId",value = "计费项id")
    private String productItemId;//
    @ApiModelProperty(name="discount",value = "折扣")
    private int discount;//

    public String getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(String productItemId) {
        this.productItemId = productItemId;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
