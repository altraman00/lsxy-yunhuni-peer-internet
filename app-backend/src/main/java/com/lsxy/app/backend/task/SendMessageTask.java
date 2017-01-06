package com.lsxy.app.backend.task;

import com.lsxy.framework.api.message.model.Message;
import com.lsxy.yunhuni.api.message.service.AccountMessageService;
import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.cache.exceptions.TransactionExecFailedException;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 定时发布活动消息
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class SendMessageTask {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageTask.class);
    @Autowired
    MessageService messageService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountMessageService accountMessageService;
    @Autowired
    RedisCacheService redisCacheService;
    /**
     * 执行上线活动消息动作
     */
    @Scheduled(cron="0 5 0/1 * * ?")
    public void sendMsg() {
        Date date=new Date();
        String hour = DateUtils.formatDate(date, "yyyy-MM-dd HH");
        String cacheKey = "scheduled_" + Thread.currentThread().getStackTrace()[1].getClassName() + "." + Thread.currentThread().getStackTrace()[1].getMethodName()+ " " + hour;
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
                redisCacheService.setTransactionFlag(cacheKey, SystemConfig.id,60*60);
                String currentCacheValue = redisCacheService.get(cacheKey);
                if(logger.isDebugEnabled()){
                    logger.debug("["+cacheKey+"]当前cacheValue:"+currentCacheValue);
                }
                if(currentCacheValue.equals(SystemConfig.id)){
                    if(logger.isDebugEnabled()){
                        logger.debug("["+cacheKey+"]马上处理该任务："+cacheKey);
                    }
                    //执行语句
                    try {
                        Long startLong = new Date().getTime();
                        String start = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH");
                        Date startTime = DateUtils.parseDate(start + ":00:00", "yyyy-MM-dd HH:mm:ss");
                        Date endTime = DateUtils.parseDate(start + ":59:59", "yyyy-MM-dd HH:mm:ss");
                        logger.info("-----------------开始执行上线活动消息动作，时间:{},参数:startTime:{}，endTime{}，type:{}，status:{}-------------------------",
                                DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"),
                                DateUtils.formatDate(startTime, "yyyy-MM-dd HH:mm:ss"),
                                DateUtils.formatDate(endTime, "yyyy-MM-dd HH:mm:ss"),
                                Message.MESSAGE_ACTIVITY,Message.NOT
                        );
                        List<Message> message = messageService.bacthUpdateStatus(startTime, endTime);
                        List<Account> list = accountService.findByStatus(Account.STATUS_NORMAL);
//                        logger.info("-----------------消息体:{}，用户对象:{}-------------------------", message,list);
                        for (int i = 0; i < message.size(); i++) {
                            accountMessageService.insertMultiple(list, message.get(i));
                        }
                        Long endLong = new Date().getTime();
                        logger.info("-----------------完成执行上线活动消息动作，时间:{},话费时长：{}-------------------------", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), endLong - startLong);
                    }catch (Exception e){
                        logger.info("-----------------执行上线活动消息动作异常，时间:{},原因：{}-------------------------", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"), e);
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
