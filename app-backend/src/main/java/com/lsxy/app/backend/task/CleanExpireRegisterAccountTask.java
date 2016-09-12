package com.lsxy.app.backend.task;

import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
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
    public void cleanExpireRegisterAccount(){


        String cacheKey = Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName();
        //执行互斥处理消息
        String flagValue = redisCacheService.get("scheduled_" + cacheKey);
        if(StringUtil.isNotEmpty(flagValue)){
            if(logger.isDebugEnabled()){
                logger.debug("["+cacheKey+"]缓存中已被设置标记，该任务被"+flagValue+"处理了");
            }
        }else{
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]准备处理该任务:"+cacheKey);
                }
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,22*60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    logger.info("开始清除过期账号！");
                    accountService.cleanExpireRegisterAccount();
                }else{
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]标记位不一致"+currentCacheValue+"  vs "+ SystemConfig.id);
                    }
                }
            } catch (TransactionExecFailedException e) {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]设置标记位异常了，该任务被另一节点处理了");
                }
            }
        }




    }

}
