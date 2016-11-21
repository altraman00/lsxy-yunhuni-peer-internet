package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.product.model.ProductType;
import com.lsxy.yunhuni.api.product.service.ProductTypeService;
import com.lsxy.yunhuni.product.dao.ProductTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/11/21.
 */
@Service
public class ProductTypeServiceImpl extends AbstractService<ProductType> implements ProductTypeService {
    @Autowired
    ProductTypeDao productTypeDao;
    @Override
    public BaseDaoInterface<ProductType, Serializable> getDao() {
        return this.productTypeDao;
    }
}
