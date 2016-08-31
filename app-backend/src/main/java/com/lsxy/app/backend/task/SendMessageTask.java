package com.lsxy.app.backend.task;

import com.lsxy.framework.api.message.service.MessageService;
import com.lsxy.framework.core.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 定时发布活动消息
 * Created by zhangxb on 2016/8/30.
 */
@Component
public class SendMessageTask {
    private static final Logger logger = LoggerFactory.getLogger(SendMessageTask.class);
    @Autowired
    MessageService messageService;

    /**
     * 执行上线活动消息动作
     */
    @Scheduled(cron="0 0 * * * ?")
    public void sendMsg(){
        Long startLong = new Date().getTime();
        String start = DateUtils.formatDate(new Date(),"yyyy-MM-dd HH");
        Date startTime = DateUtils.parseDate(start+":00:00","yyyy-MM-dd HH:mm:ss");
        Date endTime = DateUtils.parseDate(start+":59:59","yyyy-MM-dd HH:mm:ss");
        logger.info("-----------------开始执行上线活动消息动作，时间:{},参数:{}-------------------------",DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),start);
        messageService.bacthUpdateStatus(startTime,endTime);
        Long endLong = new Date().getTime();
        logger.info("-----------------完成执行上线活动消息动作，时间:{},话费时长：{}-------------------------",DateUtils.formatDate(new Date(),"yyyy-MM-dd HH:mm:ss"),endLong-startLong);
    }

}
