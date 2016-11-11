package com.lsxy.yunhuni.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrderItem;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderItemService;
import com.lsxy.yunhuni.resourceTelenum.dao.TelenumOrderItemDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/11/3.
 */
@Service
public class TelenumOrderItemServiceImpl extends AbstractService<TelenumOrderItem> implements TelenumOrderItemService {
    @Autowired
    TelenumOrderItemDao telenumOrderItemDao;
    @Override
    public BaseDaoInterface<TelenumOrderItem, Serializable> getDao() {
        return this.telenumOrderItemDao;
    }

    @Override
    public List<TelenumOrderItem> findByTenantIdAndTelenumOrderId(String tenantId, String telnumOrderId) {
        return telenumOrderItemDao.findByTenantIdAndTelnumOrderId(tenantId,telnumOrderId);
    }
}
