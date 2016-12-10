package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.product.model.ProductTenantDiscount;
import com.lsxy.yunhuni.api.product.service.ProductTenantDiscountService;
import com.lsxy.yunhuni.product.dao.ProductTenantDiscountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class ProductTenantDiscountServiceImpl extends AbstractService<ProductTenantDiscount> implements ProductTenantDiscountService {
    @Autowired
    ProductTenantDiscountDao productTenantDiscountDao;

    @Override
    public BaseDaoInterface<ProductTenantDiscount, Serializable> getDao() {
        return this.productTenantDiscountDao;
    }

    @Override
    @Cacheable(value="product",key = "'product_discount_'+#productId+'_'+#tenantId" ,unless="#result == null")
    public Double getDiscountByProductIdAndTenantId(String productId, String tenantId) {
        Double discount = null;
        ProductTenantDiscount productTenantDiscount = productTenantDiscountDao.findByProductIdAndTenantId(productId,tenantId);
        if(productTenantDiscount != null){
            discount = productTenantDiscount.getDiscount();
        }
        if(discount == null){
            discount = 1.00;
        }
        return discount;
    }

    @Override
    public ProductTenantDiscount findByProductIdAndTenantId(String productId, String tenantId) {
        return productTenantDiscountDao.findByProductIdAndTenantId(productId,tenantId);
    }
}
