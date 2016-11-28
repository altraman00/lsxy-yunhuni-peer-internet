package com.lsxy.yunhuni.api.product.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.product.model.ProductItem;

/**
 * Created by zhangxb on 2016/11/21.
 */
public interface ProductItemService extends BaseService<ProductItem> {
    ProductItem getProductItemByCode(String code);
}
