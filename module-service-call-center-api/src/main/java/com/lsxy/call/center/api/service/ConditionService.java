package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.Condition;
import com.lsxy.framework.api.base.BaseService;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface ConditionService extends BaseService<Condition> {

    public void delete(String tenantId,String appId,String conditionId);

    public Condition findOne(String tenantId,String appId,String conditionId);

    public List<Condition> getAll(String tenantId, String appId);

    public List<Condition> getAll(String tenantId, String appId,String channelId);
}
