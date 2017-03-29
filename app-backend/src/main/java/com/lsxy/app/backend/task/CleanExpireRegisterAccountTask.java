package com.lsxy.app.backend.task;

import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 清除过期的，注册没激活的账号
 * Created by liups on 2016/8/6.
 */
@Component
public class CleanExpireRegisterAccountTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanExpireRegisterAccountTask.class);

    @Autowired
    AccountService accountService;
    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0 0 * * ?")
    public void scheduled_clean_yyyyMMdd(){
        //执行语句
        logger.info("开始清除过期账号！");
        accountService.cleanExpireRegisterAccount();
    }

}
