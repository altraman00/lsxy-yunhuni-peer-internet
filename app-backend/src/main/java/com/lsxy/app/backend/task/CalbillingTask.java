package com.lsxy.app.backend.task;

import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 每日账务统计
 * Created by liups on 2016/9/3.
 */
@Component
public class CalbillingTask {
    private static final Logger logger = LoggerFactory.getLogger(CalbillingTask.class);
    @Autowired
    CalBillingService calBillingService;

    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0 2 * * ?")
    public void scheduled_calBilling_yyyyMMdd(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        calBillingService.calBilling(preDate);
    }

}
