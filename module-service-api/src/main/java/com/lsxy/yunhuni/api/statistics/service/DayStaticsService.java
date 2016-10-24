package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;

import java.util.Date;

/**
 * Created by liups on 2016/10/21.
 */
public interface DayStaticsService extends BaseService<DayStatics> {
    /**
     * 所有租户天统计
     * @param date
     */
    void dayStatics(Date date);

    /**
     * 一个租户及其应用的天统计
     * @param staticsDate 此时间要经过处理，一天的0时0分0秒0毫秒
     * @param tenant
     */
    void staticTenantAndApp(Date staticsDate, Tenant tenant);

    /**
     * 获取租户的一个日期之前（包含该日期）的最新统计
     * @param tenantId
     * @return
     */
    DayStatics getStaticByTenantId(String tenantId,Date date);
}
