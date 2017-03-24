package com.lsxy.msg.api.service;

/**
 * Created by liups on 2017/3/16.
 */
public interface MsgTaskService {
    void massTaskRequestUpdate();

    void massTaskRequestOverdueUpdate();

    void paoPaoYuMassTaskUpdate();

    void paoPaoYuUssdReSendTask();

    void qiXunTongMassTaskUpdate();
}
