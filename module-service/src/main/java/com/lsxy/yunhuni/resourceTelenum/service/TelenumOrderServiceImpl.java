package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderService;
import com.lsxy.yunhuni.resourceTelenum.dao.TelenumOrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/11/3.
 */
@Service
public class TelenumOrderServiceImpl extends AbstractService<TelenumOrder> implements TelenumOrderService {
    @Autowired
    TelenumOrderDao telenumOrderDao;
    @Override
    public BaseDaoInterface<TelenumOrder, Serializable> getDao() {
        return this.telenumOrderDao;
    }

    @Override
    public TelenumOrder findByTenantIdAndStatus(String tenantId, Integer status) {
        return telenumOrderDao.findByTenantIdAndStatus(tenantId,status);
    }
}
