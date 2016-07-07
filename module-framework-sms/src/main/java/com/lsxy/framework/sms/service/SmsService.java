package com.lsxy.framework.sms.service;

import java.util.Map;

/**
 * Created by Tandy on 2016/6/29.
 * 短信网关服务
 */
public interface SmsService {

    public boolean sendsms(String to,String template,Map<String,Object> params);

}
