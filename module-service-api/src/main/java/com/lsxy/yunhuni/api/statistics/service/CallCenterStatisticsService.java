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

    /**
     * 呼叫中心每日统计
     * @param date
     */
    void dayStatistics(Date date);

    /**
     * 呼叫中心统计
     * @param statisticsDate 统计日期
     * @param tenant 租户
     */
    void statisticsTenantAndApp(Date statisticsDate, Tenant tenant);

    /**
     * 获取某一天租户的统计，如果没有，则获取最近时间的记录
     * @param tenantId
     * @param date
     * @return
     */
    CallCenterStatistics getStatisticsByTenantId(String tenantId, Date date);

    /**
     * 往redis中插入实时数据
     * @param callCenterStatistics
     */
    void incrIntoRedis(CallCenterStatistics callCenterStatistics);

    /**
     * 获取当前时间的租户的统计数据
     * @param tenantId
     * @return
     */
    CallCenterStatistics getCurrentStatisticsByTenantId(String tenantId);

    /**
     * 获取某一天应用的统计，如果没有，则获取最近时间的记录
     * @param appId
     * @param date
     * @return
     */
    CallCenterStatistics getStatisticsByAppId(String appId, Date date);

    /**
     * 获取当前时间的应用的统计数据
     * @param appId
     * @return
     */
    CallCenterStatistics getCurrentStatisticsByAppId(String appId);

    /**
     * 获取当月的统计数据（增量）
     * @param tenantId
     * @return
     */
    CallCenterStatistics getIncStaticsOfCurrentMonthByTenantId(String tenantId);

    /**
     * 获取当日的统计数据（增量）
     * @param AppId
     * @return
     */
    CallCenterStatistics getIncStaticsOfCurrentMonthByAppId(String AppId);

    /**
     * 座席每月扣费
     */
    void agentMonthTask();
}
