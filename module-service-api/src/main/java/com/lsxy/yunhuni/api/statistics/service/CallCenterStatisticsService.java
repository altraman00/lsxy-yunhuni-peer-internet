package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;

import java.util.Date;

/**
 * 呼叫中心统计
 * Created by liups on 2016/11/7.
 */
public interface CallCenterStatisticsService extends BaseService<CallCenterStatistics> {

    void dayStatistics(Date date);

    void statisticsTenantAndApp(Date statisticsDate, Tenant tenant);

    CallCenterStatistics getStatisticsByTenantId(String tenantId, Date date);

    void incrIntoRedis(CallCenterStatistics callCenterStatistics,Date date);

    CallCenterStatistics getCurrentStatisticsByTenantId(String tenantId);

    CallCenterStatistics getStatisticsByAppId(String appId, Date date);

    CallCenterStatistics getCurrentStatisticsByAppId(String appId);

    CallCenterStatistics getIncStaticsOfCurrentMonthByTenantId(String tenantId);

    CallCenterStatistics getIncStaticsOfCurrentMonthByAppId(String AppId);
}
