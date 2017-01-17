package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 放音文件上传遗漏补救方案
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class VoiceFilePlaySyncTask {
    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlaySyncTask.class);
    @Autowired
    private MQService mqService;
    @Autowired
    private RedisCacheService redisCacheService;
    /**
     * 通知进行放音文件同步操作
     */
    @Scheduled(cron="0 0/30 * * * ?")
    public void startSync() {
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
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,60*30);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    if(logger.isDebugEnabled()){
                        logger.debug("启动放音文件上传遗漏补救方案--开始");
                    }
                    mqService.publish(new VoiceFilePlayAuditCompletedEvent());
                    if(logger.isDebugEnabled()){
                        logger.debug("启动放音文件上传遗漏补救方案--已通知--结束");
                    }
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
