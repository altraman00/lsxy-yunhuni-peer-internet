package com.lsxy.framework.sms.service;

import com.lsxy.framework.sms.exceptions.CheckCodeNotFoundException;
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

    /**
     * 生成验证码并存到Redis（不负责发送）
     * @param to 手机号
     * @return
     * @throws TooManyGenTimesException
     */
    public String genVC(String to) throws TooManyGenTimesException;

    /**
     * 校验验证码
     * @param to 手机号
     * @param vc 验证码
     * @return
     * @throws InvalidValidateCodeException 验证码错误
     * @throws CheckOutMaxTimesException 校验次数太多
     * @throws CheckCodeNotFoundException 验证码超时或不存在
     */
    public boolean checkVC(String to,String vc) throws InvalidValidateCodeException,CheckOutMaxTimesException,CheckCodeNotFoundException;


}
