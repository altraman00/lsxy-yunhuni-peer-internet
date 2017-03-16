package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.MsgDay;
import com.lsxy.yunhuni.api.statistics.model.MsgStatisticsVo;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/14.
 */
public interface MsgDayService extends BaseService<MsgDay> {
    void dayStatistics(Date date);

    Long getUsed(String tenantId, String appId, String subaccountId, String sendType, Date startTime, Date endTime);
    Page<MsgStatisticsVo> getStatisticsPage(String tenantId, String appId , Date date1, Date date2, Integer pageNo, Integer pageSize);
    List<MsgStatisticsVo> getStatisticsList(String tenantId, String appId , Date date1, Date date2);
}
