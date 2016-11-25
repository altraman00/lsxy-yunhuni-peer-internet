package com.lsxy.app.backend.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by liups on 2016/11/25.
 */
@Profile("test")
@Component
public class ExtensionRegisterImitator {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionRegisterImitator.class);
    @Reference(timeout=3000,check = false,lazy = true)
    AppExtensionService appExtensionService;
    @Autowired
    RedisCacheService redisCacheService;

    @Scheduled(cron="0 0 2 1 * ? ")
    public void month(){
        Date date=new Date();
        String month = DateUtils.formatDate(date, "yyyy-MM-dd HH:mm");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName() + " " + month;
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
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,10 * 60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    this.registerAll();
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

    private void registerAll(){
        Iterable<AppExtension> list = appExtensionService.list();
        list.forEach(ex -> appExtensionService.register(ex.getId()));
    }

}
