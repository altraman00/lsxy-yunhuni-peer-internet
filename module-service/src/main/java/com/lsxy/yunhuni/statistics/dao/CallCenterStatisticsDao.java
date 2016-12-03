package com.lsxy.yunhuni.statistics.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * Created by liups on 2016/11/7.
 */
public interface CallCenterStatisticsDao extends BaseDaoInterface<CallCenterStatistics,Serializable> {
    CallCenterStatistics findFirstByTenantIdAndAppIdIsNullOrderByDtDesc(String id);

    CallCenterStatistics findFirstByAppIdOrderByDtDesc(String id);

    CallCenterStatistics findFirstByTenantIdAndDtAndAppIdIsNull(String tenantId, Date dt);

    CallCenterStatistics findFirstByTenantIdAndDtLessThanAndAppIdIsNullOrderByDtDesc(String tenantId, Date dt);

    CallCenterStatistics findFirstByAppIdAndDt(String appId, Date dt);

    CallCenterStatistics findFirstByAppIdAndDtLessThanOrderByDtDesc(String appId, Date dt);
}
