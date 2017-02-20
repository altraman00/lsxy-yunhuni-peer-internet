package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * Created by liups on 2017/2/20.
 */
public class QuotaDayStatisticsTask {
    private static final Logger logger = LoggerFactory.getLogger(QuotaDayStatisticsTask.class);

    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0 4 * * ?")
    public void dayStatics(){
        Date date=new Date();
        String day = DateUtils.formatDate(date, "yyyy-MM-dd");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " " + day;
        //执行互斥处理消息
        String flagValue = redisCacheService.get( cacheKey);
        if(StringUtil.isNotEmpty(flagValue)){
            if(logger.isDebugEnabled()){
                logger.debug("["+cacheKey+"]缓存中已被设置标记，该任务被"+flagValue+"处理了");
            }
        }else{
            try {
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]准备处理该任务:"+cacheKey);
                }
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,24*60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    Date preDate = DateUtils.getPreDate(date);
//                    dayStaticsService.dayStatics(preDate);
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
