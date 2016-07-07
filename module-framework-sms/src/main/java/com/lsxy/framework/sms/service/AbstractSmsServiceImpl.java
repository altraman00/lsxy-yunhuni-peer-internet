package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * Created by Tandy on 2016/7/7.
 */
public abstract class AbstractSmsServiceImpl implements  SmsService{

    @Autowired
    private SMSSendLogService smsSendLogService;

    /**
     * 根据制定模板构建短信内容
     * @param template
     * @param params
     * @return
     */
    protected String buildSmsContent(String template, Map<String, Object> params) {
        //临时直接返回模板，后面调整为velocity解析
        return template;
    }

    /**
     * 异步入库
     * @param smsSendLog
     */
    @Async
    protected void saveToDB(SMSSendLog smsSendLog){
        smsSendLogService.save(smsSendLog);
    }

}
