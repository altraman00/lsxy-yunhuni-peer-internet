package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.Condition;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/22.
 */
public interface ConditionService extends BaseService<Condition> {

    public Condition save(String tenantId,String appId,Condition condition) throws YunhuniApiException;

    public void delete(String tenantId,String appId,String conditionId) throws YunhuniApiException;

    public Condition findOne(String tenantId,String appId,String conditionId) throws YunhuniApiException;

    public List<Condition> getAll(String tenantId, String appId) throws YunhuniApiException;

    public List<Condition> getAll(String tenantId, String appId,String channelId) throws YunhuniApiException;
}
