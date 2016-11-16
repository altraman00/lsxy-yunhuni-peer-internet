package com.lsxy.call.center.dao;

import com.lsxy.call.center.api.model.Condition;
import com.lsxy.framework.api.base.BaseDaoInterface;
import java.util.List;

import java.io.Serializable;

/**
 * 呼叫中心系统
 * Created by zhangxb on 2016/10/22.
 */
public interface ConditionDao extends BaseDaoInterface<Condition, Serializable> {

    public List<Condition> findByTenantIdAndAppId(String tenantId,String appId);

    public List<Condition> findByTenantIdAndAppIdAndChannelId(String tenantId,String appId,String channelId);
}
