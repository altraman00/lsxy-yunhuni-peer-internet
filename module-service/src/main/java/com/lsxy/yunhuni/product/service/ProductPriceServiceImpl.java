package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.service.ProductPriceService;
import com.lsxy.yunhuni.product.dao.ProductPriceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class ProductPriceServiceImpl extends AbstractService<ProductPrice> implements ProductPriceService {
    @Autowired
    ProductPriceDao  productPriceDao;

    @Override
    public BaseDaoInterface<ProductPrice, Serializable> getDao() {
        return this.productPriceDao;
    }


    @Override
    @Cacheable(value="product",key = "'product_price_'+#productId" ,unless="#result == null")
    public ProductPrice getAvailableProductPrice(String productId) {
        ProductPrice productPrice = productPriceDao.findFirstByProductItemIdAndStatusOrderByPriorityDesc(productId,ProductPrice.STATUS_VALID);
        if(productPrice == null){
            throw new RuntimeException("产品资费不存在");
        }
        return productPrice;
    }

    @Override
    public Page<ProductPrice> getPageOrderCreate(Integer pageNo,Integer pageSize) {
        String hql = "FROM ProductPrice obj ORDER BY obj.createTime DESC ";
        return pageList(hql,pageNo,pageSize);
    }
}
