package com.lsxy.yunhuni.api.product.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.product.model.ProductTenantDiscount;

/**
 * 产品折扣服务接口
 * Created by liups on 2016/8/27.
 */
public interface ProductTenantDiscountService extends BaseService<ProductTenantDiscount> {
    /**
     * 取出租户某产品的折扣
     * @param productId
     * @param tenantId
     * @return
     */
    Double getDiscountByProductIdAndTenantId(String productId,String tenantId);
    ProductTenantDiscount findByProductIdAndTenantId(String productId,String tenantId);
}
