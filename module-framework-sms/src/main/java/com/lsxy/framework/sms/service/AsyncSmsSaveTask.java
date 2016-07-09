package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/9.
 */
@Component
public class AsyncSmsSaveTask {


    @Autowired
    private SMSSendLogService smsSendLogService;

    /**
     * 异步入库
     * @param smsSendLog
     */
    @Async
    public void saveToDB(SMSSendLog smsSendLog){
        smsSendLogService.save(smsSendLog);
    }


}
