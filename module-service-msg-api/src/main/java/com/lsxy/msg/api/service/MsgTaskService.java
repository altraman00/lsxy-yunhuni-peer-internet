package com.lsxy.msg.api.service;

import org.springframework.scheduling.annotation.Scheduled;

/**
 * Created by liups on 2017/3/16.
 */
public interface MsgTaskService {
    @Scheduled(cron="0 0/12 * * * ?")
    void massTaskRequestUpdate();

    @Scheduled(cron="0 0/10 * * * ?")
    void paoPaoYuMassTaskUpdate();

    @Scheduled(cron="0 0/1 * * * ?")
    void paoPaoYuUssdReSendTask();

    @Scheduled(cron="0 0/10 * * * ?")
    void qiXunTongMassTaskUpdate();
}
