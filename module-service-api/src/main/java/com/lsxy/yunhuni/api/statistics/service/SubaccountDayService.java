package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/2/21.
 */
public interface SubaccountDayService extends BaseService<SubaccountDay> {
    Page<SubaccountStatisticalVO> getPageByConditions(Integer pageNo, Integer pageSize, Date startTime, Date endTime, String tenantId, String appId, String subaccountId);
    List<SubaccountStatisticalVO> getListByConditions(Date startTime, Date endTime, String tenantId, String appId, String subaccountId);
    //统计总消费金额和话务量
    Map sum(Date start, Date end, String tenantId, String appId, String subaccountId);
    void dayStatistics(Date date);
}
