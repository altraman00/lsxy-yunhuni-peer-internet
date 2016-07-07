package com.lsxy.framework.sms.clients;

import org.springframework.stereotype.Component;

/**
 * Created by Tandy on 2016/7/7.
 * SMSClient工厂类，为以后扩展其他短信网关留有扩展方案
 */
@Component
public class SMSClientFactory {

    private SMSClient smsClient;

    public SMSClient getSMSClient(){
        if(smsClient == null){
            smsClient = new SMSClientSY();
        }
        return smsClient;
    }
}
