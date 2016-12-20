package com.lsxy.app.oc.rest.config.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;

/**
 * Created by zhangxb on 2016/11/26.
 */
@ApiModel
public class ProductEditVo {
    @ApiModelProperty(name="priceItem",value = "计费项")
    private String priceItem;//计费项
    @ApiModelProperty(name="price",value = "价格")
    private BigDecimal price;//
    @ApiModelProperty(name="unit",value = "计价单位")
    private String unit;//计价单位

    public String getPriceItem() {
        return priceItem;
    }

    public void setPriceItem(String priceItem) {
        this.priceItem = priceItem;
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
