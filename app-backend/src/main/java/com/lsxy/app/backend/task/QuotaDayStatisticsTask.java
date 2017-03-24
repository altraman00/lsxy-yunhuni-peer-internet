package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liups on 2017/2/20.
 */
@Component
public class QuotaDayStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(QuotaDayStatisticsTask.class);

    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    CertAccountQuotaService certAccountQuotaService;

    @Scheduled(cron="0 0 4 * * ?")
    public void scheduled_dayStatics_yyyyMMdd(){
        Date date=new Date();
        //执行语句
        Date preDate = DateUtils.getPreDate(date);
        certAccountQuotaService.dayStatics(preDate);
    }

}
