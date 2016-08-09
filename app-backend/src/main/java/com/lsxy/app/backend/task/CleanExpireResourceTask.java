package com.lsxy.app.backend.task;

import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2016/8/8.
 */
@Component
public class CleanExpireResourceTask {

    @Autowired
    ResourcesRentService resourcesRentService;

    /**
     * 清除过期号码
     */
    @Scheduled(cron="0 0 0 * * ?")
    public void cleanExpireResource(){
        resourcesRentService.cleanExpireTelnumResourceRent();
    }

}
