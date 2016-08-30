package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.service.ProductService;
import com.lsxy.yunhuni.product.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class ProductServiceImpl extends AbstractService<Product> implements ProductService {
    @Autowired
    ProductDao productDao;

    @Override
    public BaseDaoInterface<Product, Serializable> getDao() {
        return this.productDao;
    }


    @Override
    @Cacheable(value="product",key = "'product_'+#code" ,unless="#result == null")
    public Product getProductByCode(String code) {
        Product product = productDao.findByCode(code);
        if(product == null){
            throw new RuntimeException("产品不存在");
        }
        return product;
    }
}
