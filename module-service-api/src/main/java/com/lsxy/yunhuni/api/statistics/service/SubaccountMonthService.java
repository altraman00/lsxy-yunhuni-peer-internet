package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;

import java.util.Date;
import java.util.Map;

/**
 * Created by liups on 2017/2/21.
 */
public interface SubaccountMonthService extends BaseService<SubaccountMonth> {
    Page<SubaccountStatisticalVO> getPageByConditions(Integer pageNo, Integer pageSize, Date startTime, Date endTime, String tenantId, String appId,String subaccountId);
    //统计总消费金额和话务量
    Map sum(Date start, Date end, String tenantId, String appId, String subaccountId);
}
