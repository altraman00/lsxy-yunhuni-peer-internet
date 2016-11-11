package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrderItem;

import java.util.List;

/**
 * Created by zhangxb on 2016/11/3.
 */
public interface TelenumOrderItemService extends BaseService<TelenumOrderItem> {
    List<TelenumOrderItem> findByTenantIdAndTelenumOrderId(String tenantId, String telnumOrderId);
}
