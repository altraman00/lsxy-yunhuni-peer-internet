package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.product.service.ProductItemService;
import com.lsxy.yunhuni.product.dao.ProductItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

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
}
