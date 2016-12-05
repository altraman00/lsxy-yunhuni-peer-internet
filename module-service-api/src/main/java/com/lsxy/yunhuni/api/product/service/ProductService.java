package com.lsxy.yunhuni.api.product.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.product.model.Product;

/**
 * 产品服务接口
 * Created by liups on 2016/8/27.
 */
public interface ProductService extends BaseService<Product> {
    /**
     * 根据产品标识取出相应的产品
     * @param code
     * @return
     */
    Product getProductByCode(String code);

}
