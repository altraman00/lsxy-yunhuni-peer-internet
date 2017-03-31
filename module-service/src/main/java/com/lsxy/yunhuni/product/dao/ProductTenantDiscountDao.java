package com.lsxy.yunhuni.product.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.product.model.ProductTenantDiscount;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/8/27.
 */
public interface ProductTenantDiscountDao extends BaseDaoInterface<ProductTenantDiscount, Serializable> {
    /**
     * 根据产品ID和租户Id获取折扣
     * @param productId
     * @param tenantId
     * @return
     */
    ProductTenantDiscount findByProductIdAndTenantId(String productId, String tenantId);
    List<ProductTenantDiscount> findByTenantId(String tenantId);
}
