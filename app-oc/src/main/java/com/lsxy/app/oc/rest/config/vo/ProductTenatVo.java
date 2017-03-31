package com.lsxy.app.oc.rest.config.vo;

import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.model.ProductTenantDiscount;

/**
 * Created by zhangxb on 2016/11/26.
 */
public class ProductTenatVo {
    private String id;//
    private String name;//
    private String basePrice;//基础价格
    private String discount;//折扣
    private String buyoutPrice;//一口价

    public ProductTenatVo(String id, String name, String basePrice, String discount, String buyoutPrice) {
        this.id = id;
        this.name = name;
        this.basePrice = basePrice;
        this.discount = discount;
        this.buyoutPrice = buyoutPrice;
    }

    public static ProductTenatVo initProductTenatVo(ProductPrice productPrice, ProductTenantDiscount productTenantDiscount){
        String id = productPrice.getProductItem().getId();
        String name = productPrice.getProductItem().getProduct().getName() +"-"+ productPrice.getProductItem().getName();
        String basePrice =  productPrice.getPrice() +"/"+ (productPrice.getTimeUnit()==1?"":productPrice.getTimeUnit())+productPrice.getUnit();
        String discount = "";
        String buyoutPrice = "";
        if(productTenantDiscount != null){
            buyoutPrice = productTenantDiscount.getBuyoutPrice()!=null?productTenantDiscount.getBuyoutPrice().toString():"";
            discount = productTenantDiscount.getDiscount()!=null?getDoubleToStr(productTenantDiscount.getDiscount()):"";
        }
        return new ProductTenatVo( id,  name,  basePrice,  discount,  buyoutPrice);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(String basePrice) {
        this.basePrice = basePrice;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getBuyoutPrice() {
        return buyoutPrice;
    }

    public void setBuyoutPrice(String buyoutPrice) {
        this.buyoutPrice = buyoutPrice;
    }

    private static String  getDoubleToStr(double num){
        num *= 100;
        String str = "";
        if(num % 1.0 == 0){
            str += (long)num;
        }else{
            str += num;
        }
        return str;
    }
}
