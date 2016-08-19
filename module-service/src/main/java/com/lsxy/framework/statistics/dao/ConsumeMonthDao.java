package com.lsxy.framework.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeMonth;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 消费月统计DAO
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeMonthDao extends BaseDaoInterface<ConsumeMonth,Serializable> {
    /**
     * 获取用户开始消费的第一个月统计数据
     * @param tenantId
     * @return
     */
    ConsumeMonth findFirst1ByTenantIdOrderByDtAsc(String tenantId);

    /**
     * 获取用户的消费月账单
     * @param tenantId 租户Id
     * @param dt 时间
     * @param appId 应用Id
     * @return
     */
    List<ConsumeMonth> findByTenantIdAndDtAndAppIdAndTypeNotNull(String tenantId, Date dt, String appId);

    /**
     * 获取用户的消费月账单 （所有应用）
     * @param tenantId
     * @param dt
     * @return
     */
    List<ConsumeMonth> findByTenantIdAndDtAndAppIdIsNullAndTypeNotNull(String tenantId, Date dt);
}
