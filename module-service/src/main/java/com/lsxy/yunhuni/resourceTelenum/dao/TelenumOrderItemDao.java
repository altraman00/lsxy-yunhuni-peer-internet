package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhangxb on 2016/11/3.
 */
public interface TelenumOrderItemDao extends BaseDaoInterface<TelenumOrderItem, Serializable> {
    List<TelenumOrderItem> findByTenantIdAndTelnumOrderId(String tenantId, String telnumOrderId);
}
