package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 清除租户租用过期的号码，解除与租户的绑定
 * Created by liups on 2016/8/8.
 */
@Component
public class CleanExpireResourceTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanExpireResourceTask.class);
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    RedisCacheService redisCacheService;
    /**
     * 清除过期号码
     */
    @Scheduled(cron="0 0 3 * * ?")
    public void scheduled_clean_yyyyMMdd(){
        //执行语句
        resourcesRentService.cleanExpireTelnumResourceRent();

    }

}
