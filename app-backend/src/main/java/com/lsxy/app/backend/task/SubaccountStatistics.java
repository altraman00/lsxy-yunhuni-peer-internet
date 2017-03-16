package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liups on 2017/2/21.
 */
@Component
public class SubaccountStatistics {
    private static final Logger logger = LoggerFactory.getLogger(SubaccountStatistics.class);
    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    SubaccountDayService subaccountDayService;
    @Autowired
    SubaccountMonthService subaccountMonthService;

    @Scheduled(cron="0 30 3 * * ?")
    public void scheduled_dayStatistics_yyyyMMdd(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        subaccountDayService.dayStatistics(preDate);
    }

    /**
     * 每月1号2：00：00触发执行
     */
    @Scheduled(cron="0 30 4 1 * ? ")
    public void scheduled_month_yyyyMM(){
        Date date=new Date();
        //执行语句
        Date prevMonth = DateUtils.getPrevMonth(date);
        subaccountMonthService.monthStatistics(prevMonth);

    }

}
