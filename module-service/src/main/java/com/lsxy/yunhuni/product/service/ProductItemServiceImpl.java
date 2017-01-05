package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.product.service.ProductItemService;
import com.lsxy.yunhuni.product.dao.ProductItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangxb on 2016/11/21.
 */
@Service
public class ProductItemServiceImpl extends AbstractService<ProductItem> implements ProductItemService {
    @Autowired
    ProductItemDao productTypeDao;
    @Override
    public BaseDaoInterface<ProductItem, Serializable> getDao() {
        return this.productTypeDao;
    }
    @Override
    @Cacheable(value="product",key = "'product_item_'+#code" ,unless="#result == null")
    public ProductItem getProductItemByCode(String code) {
        ProductItem product = productTypeDao.findByCode(code);
        if(product == null){
            throw new RuntimeException("产品不存在");
        }
        return product;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true),
                    @CacheEvict(value = "product", key = "'product_item_' + #entity.code", beforeInvocation = true)
            }
    )
    public ProductItem save(ProductItem entity) {
        //更新最后修改时间
        entity.setLastTime(new Date());
        return getDao().save(entity);
    }
}
