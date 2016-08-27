package com.lsxy.yunhuni.product.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.product.model.Product;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/27.
 */
public interface ProductDao extends BaseDaoInterface<Product, Serializable> {
    /**
     * 根据产品标识查出对应的产品
     * @param code
     * @return
     */
    Product findByCode(String code);
}
