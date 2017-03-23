package com.lsxy.app.backend.task;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.msg.api.service.MsgTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by liups on 2017/3/16.
 */
@Component
public class MsgTaskCheckTask {
    private static final Logger logger = LoggerFactory.getLogger(MsgTaskCheckTask.class);
    @Reference(timeout=3000,check = false,lazy = true)
    MsgTaskService msgTaskService;

    @Scheduled(cron="0 0/12 * * * ?")
    public void scheduled_massTaskRequest_yyyyMMddHHmm(){
        msgTaskService.massTaskRequestUpdate();
    }


    @Scheduled(cron="0 30 * * * ?")
    public void scheduled_massTaskRequestOverdue_yyyyMMddHH(){
        msgTaskService.massTaskRequestOverdueUpdate();
    }

    @Scheduled(cron="0 0/10 * * * ?")
    public void scheduled_paoPaoYuMassTask_yyyyMMddHHmm(){
        msgTaskService.paoPaoYuMassTaskUpdate();
    }

    @Scheduled(cron="0 0/1 * * * ?")
    public void scheduled_paoPaoYuUssdReSend_yyyyMMddHHmm(){
        msgTaskService.paoPaoYuUssdReSendTask();
    }

    @Scheduled(cron="0 0/10 * * * ?")
    public void scheduled_qiXunTongMassTask_yyyyMMddHHmm(){
        msgTaskService.qiXunTongMassTaskUpdate();
    }


}
