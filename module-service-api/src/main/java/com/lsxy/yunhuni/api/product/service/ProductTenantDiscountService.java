package com.lsxy.yunhuni.api.product.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.product.model.ProductTenantDiscount;

/**
 * 产品折扣服务接口
 * Created by liups on 2016/8/27.
 */
public interface ProductTenantDiscountService extends BaseService<ProductTenantDiscount> {
    Double getDiscountByProductIdAndTenantId(String productId,String tenantId);
}
