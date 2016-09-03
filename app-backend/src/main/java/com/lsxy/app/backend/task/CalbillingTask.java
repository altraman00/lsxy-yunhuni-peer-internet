package com.lsxy.app.backend.task;

import com.lsxy.yunhuni.api.billing.service.CalBillingService;
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
    @Autowired
    CalBillingService calBillingService;
    @Scheduled(cron="0 0 2 * * ?")
    public void calBilling(){
        calBillingService.calBilling(new Date());
    }

}
