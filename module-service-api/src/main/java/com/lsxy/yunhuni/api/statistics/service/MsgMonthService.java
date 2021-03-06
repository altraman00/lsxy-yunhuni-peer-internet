package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.MsgMonth;
import com.lsxy.yunhuni.api.statistics.model.MsgStatisticsVo;

import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2017/3/14.
 */
public interface MsgMonthService extends BaseService<MsgMonth> {
    void monthStatistics(Date date);
    Page<MsgStatisticsVo> getStatisticsPage(String tenantId, String appId , Date date1, Date date2, Integer pageNo, Integer pageSize);
    List<MsgStatisticsVo> getStatisticsList(String tenantId, String appId , Date date1, Date date2);
}
