package com.lsxy.yunhuni.resourceTelenum.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/11/3.
 */
public interface TelenumOrderDao extends BaseDaoInterface<TelenumOrder, Serializable> {
    TelenumOrder findByTenantIdAndStatus(String tenantId,Integer status);
}
