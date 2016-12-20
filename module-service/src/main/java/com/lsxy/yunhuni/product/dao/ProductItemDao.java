package com.lsxy.yunhuni.product.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.product.model.ProductItem;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/11/21.
 */
public interface ProductItemDao extends BaseDaoInterface<ProductItem, Serializable> {
    /**
     * 根据产品标识查出对应的产品
     * @param code
     * @return
     */
    ProductItem findByCode(String code);
}
