package com.lsxy.app.backend.task;

import com.lsxy.framework.api.tenant.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2016/8/6.
 */
@Component
public class CleanExpireRegisterAccountTask {
    private static final Logger logger = LoggerFactory.getLogger(CleanExpireRegisterAccountTask.class);

    @Autowired
    AccountService accountService;

    @Scheduled(cron="0 0 0 * * ?")
    public void cleanExpireRegisterAccount(){
        logger.info("开始清除过期账号！");
        accountService.cleanExpireRegisterAccount();
    }

}
