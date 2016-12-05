package com.lsxy.yunhuni.product.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.product.model.ProductPrice;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/27.
 */
public interface ProductPriceDao extends BaseDaoInterface<ProductPrice, Serializable> {
    ProductPrice findFirstByProductItemIdAndStatusOrderByPriorityDesc(String productItemId, int statusValid);
}
