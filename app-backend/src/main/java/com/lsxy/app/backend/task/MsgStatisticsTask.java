package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.service.MsgDayService;
import com.lsxy.yunhuni.api.statistics.service.MsgMonthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liups on 2017/3/16.
 */
@Component
public class MsgStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(MsgStatisticsTask.class);
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    MsgDayService msgDayService;
    @Autowired
    MsgMonthService msgMonthService;

    /**
     * 每月1号2：00：00触发执行
     */
    @Scheduled(cron="0 0 2 1 * ? ")
    public void scheduled_month_yyyyMM(){
        Date date=new Date();
        //执行语句
        Date prevMonth = DateUtils.getPrevMonth(date);
        msgMonthService.monthStatistics(prevMonth);
    }

    @Scheduled(cron="0 0 3 * * ?")
    public void scheduled_dayStatics_yyyyMMdd(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        msgDayService.dayStatistics(preDate);
    }

}
