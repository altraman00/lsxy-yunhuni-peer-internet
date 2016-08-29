package com.lsxy.yunhuni.product.service;

import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.product.service.ProductPriceService;
import com.lsxy.yunhuni.api.product.service.ProductService;
import com.lsxy.yunhuni.api.product.service.ProductTenantDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class CalCostServiceImpl implements CalCostService{
    @Autowired
    ProductService productService;
    @Autowired
    ProductPriceService productPriceService;
    @Autowired
    ProductTenantDiscountService productTenantDiscountService;

    @Override
    public BigDecimal calCost(String code, String tenantId, Long time) {
        BigDecimal cost;
        Product product = productService.getProductByCode(code);
        ProductPrice productPrice = productPriceService.getAvailableProductPrice(product.getId());
        Double discount = productTenantDiscountService.getDiscountByProductIdAndTenantId(product.getId(), tenantId);
        if(product.getCalType() == Product.CAL_TYPE_NUM){
            //如果是计量，则只需单价*折扣
            cost = productPrice.getPrice().multiply(new BigDecimal(discount));
        }else{
            //如果是计时，则只需（时长/时长单位（不满一个时长单位按一个时长单位算））*单价*折扣
            BigDecimal calNum  = new BigDecimal(time).divide(new BigDecimal(product.getTimeUnit())).setScale(0, BigDecimal.ROUND_UP);
            cost = calNum.multiply(productPrice.getPrice()).multiply(new BigDecimal(discount));
        }
        return cost;
    }


}
