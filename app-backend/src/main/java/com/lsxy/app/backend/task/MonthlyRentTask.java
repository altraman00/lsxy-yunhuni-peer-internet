package com.lsxy.app.backend.task;

import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 月租费定时任务
 * Created by liups on 2016/9/17.
 */
@Component
public class MonthlyRentTask {
    private static final Logger logger = LoggerFactory.getLogger(RechargeStatisticsTask.class);
    @Autowired
    ResourcesRentService resourcesRentService;
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    CallCenterStatisticsService callCenterStatisticsService;

    /**
     * 每天执行，有钱就扣
     */
    @Scheduled(cron="0 30 0 * * ?")
    public void scheduled_monthlyResourcesRentTask_yyyyMMdd(){
        //执行语句
        resourcesRentService.resourcesRentTask();
    }


    /**
     * 每月执行
     */
    @Scheduled(cron="0 30 4 1 * ?")
    public void scheduled_monthlyRecordTask_yyyyMM(){
        //执行语句
        voiceFileRecordService.recordingVoiceFileTask();
    }

    /**
     * 每月执行
     */
    @Scheduled(cron="0 30 4 1 * ?")
    public void scheduled_monthlyAgentTask_yyyyMM(){
        //执行语句
        callCenterStatisticsService.agentMonthTask();
    }

}
