package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liups on 2016/10/21.
 */
public interface DayStaticsDao extends BaseDaoInterface<DayStatics,Serializable> {
    /**
     * 获取租户最新的天统计
     * @param tenantId
     * @return
     */
    DayStatics findFirstByTenantIdAndAppIdIsNullOrderByDtDesc(String tenantId);

    /**
     * 获取app最新的天统计
     * @param appId
     * @return
     */
    DayStatics findFirstByAppIdOrderByDtDesc(String appId);

    /**
     * 获取租户某一天的统计
     * @param tenantId
     * @param dt
     * @return
     */
    DayStatics findFirstByTenantIdAndDtAndAppIdIsNull(String tenantId, Date dt);

    /**
     * 获取租户某一天以前（包含这一天）最新的天统计
     * @param tenantId
     * @param dt
     * @return
     */
    DayStatics findFirstByTenantIdAndDtLessThanAndAppIdIsNullOrderByDtDesc(String tenantId, Date dt);
}
