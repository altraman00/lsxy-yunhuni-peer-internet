package com.lsxy.framework.sms.service;

import com.lsxy.framework.sms.exceptions.CheckCodeExpireException;
import com.lsxy.framework.sms.exceptions.CheckOutMaxTimesException;
import com.lsxy.framework.sms.exceptions.InvalidValidateCodeException;
import com.lsxy.framework.sms.exceptions.TooManyGenTimesException;

import java.util.Map;

/**
 * Created by Tandy on 2016/6/29.
 * 短信网关服务
 */
public interface SmsService {
    String CODE_PREFIX = "MC_";

    /**
     * 发送模板短信
     * @param to
     * @param template 模板标识
     * @param params
     * @return
     */
    public boolean sendsms(String to,String template,Map<String,Object> params);

    public String genVC(String to) throws TooManyGenTimesException;

    public boolean checkVC(String to,String vc) throws InvalidValidateCodeException,CheckOutMaxTimesException,CheckCodeExpireException;


}
