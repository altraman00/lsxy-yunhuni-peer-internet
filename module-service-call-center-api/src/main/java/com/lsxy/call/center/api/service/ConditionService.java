package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.Condition;
import com.lsxy.framework.api.base.BaseService;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface ConditionService extends BaseService<Condition> {

    public void delete(String tenantId,String appId,String conditionId);
}
