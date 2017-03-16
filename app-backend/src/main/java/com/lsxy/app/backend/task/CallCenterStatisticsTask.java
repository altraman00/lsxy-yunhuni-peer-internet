package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liups on 2016/12/3.
 */
@Component
public class CallCenterStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(CallCenterStatisticsTask.class);
    @Autowired
    CallCenterStatisticsService callCenterStatisticsService;

    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0 2 * * ?")
    public void scheduled_callCenterStatistics_yyyyMMdd(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        callCenterStatisticsService.dayStatistics(preDate);
    }

}
