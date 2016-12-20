package com.lsxy.yunhuni.api.resourceTelenum.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;

/**
 * Created by zhangxb on 2016/11/3.
 */
public interface TelenumOrderService extends BaseService<TelenumOrder> {
    /**
     * 获取租户的订单
     * @param tenantId
     * @return
     */
    TelenumOrder findByTenantIdAndStatus(String tenantId,Integer status);
}
