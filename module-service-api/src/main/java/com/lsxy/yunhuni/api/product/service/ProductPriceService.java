package com.lsxy.yunhuni.api.product.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.product.model.ProductPrice;

/**
 * 产品资费接口
 * Created by liups on 2016/8/27.
 */
public interface ProductPriceService extends BaseService<ProductPrice> {
    /**
     * 根据产品ID取出当前有效的产品资费
     * @param productId
     * @return
     */
    ProductPrice getAvailableProductPrice(String productId);
    Page<ProductPrice> getPageOrderCreate(Integer pageNo,Integer pageSize);
}
