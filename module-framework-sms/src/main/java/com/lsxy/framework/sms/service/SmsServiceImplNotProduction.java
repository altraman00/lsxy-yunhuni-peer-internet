package com.lsxy.framework.sms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.sms.clients.SMSClientFactory;
import com.lsxy.framework.sms.exceptions.CheckCodeNotFoundException;
import com.lsxy.framework.sms.exceptions.CheckOutMaxTimesException;
import com.lsxy.framework.sms.exceptions.InvalidValidateCodeException;
import com.lsxy.framework.sms.exceptions.TooManyGenTimesException;
import com.lsxy.framework.sms.model.MobileCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Random;

/**
 * Created by Tandy on 2016/6/29.
 * 非生产环境使用
 * 非生产环境直接入库出库
 *
 */

@Profile({"local","development","test"})
@Component
public class SmsServiceImplNotProduction extends AbstractSmsServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(SmsServiceImplNotProduction.class);

    @Autowired
    RedisCacheService redisCacheService;

    @Autowired
    private SMSClientFactory smsClientFactory;

    @Autowired
    private AsyncSmsSaveTask asyncSmsSaveTask;

    @Override
    public boolean sendsms(String to, String template, Map<String, Object> params) {
        String content = buildSmsContent(template,params);

        if(logger.isDebugEnabled()){
            logger.debug("非生产环境短信发送：{}",to);
            logger.debug("发送内容：{}",content);
        }

        //如果短信发送成功就异步存到数据库
        String clientName = smsClientFactory.getSMSClient().getClientName();
        SMSSendLog log = new SMSSendLog(to,content,clientName);
        asyncSmsSaveTask.saveToDB(log);
        return true;
    }

    @Override
    public String genVC(String to) throws TooManyGenTimesException {
        String getJson = redisCacheService.get(CODE_PREFIX + to);
        if(StringUtils.isNotBlank(getJson)){
            MobileCode getCode = JSONUtil2.fromJson(getJson, MobileCode.class);
            if(getCode != null){
                if(System.currentTimeMillis() < (getCode.getCreateTime() + MobileCode.TIME_INTERVAL)){
                    throw new TooManyGenTimesException("验证码生成过于频繁");
                }
            }
        }
        String random = random(4);
        MobileCode mobileCode = new MobileCode(to,random);
        String setJson = JSONUtil2.objectToJson(mobileCode);
        redisCacheService.set(CODE_PREFIX + to,setJson , 30 * 60);
        return random;
    }

    @Override
    public boolean checkVC(String to, String vc) throws InvalidValidateCodeException, CheckOutMaxTimesException, CheckCodeNotFoundException {
        if(StringUtils.isNotBlank(to) && StringUtils.isNotBlank(vc)){
            String json = redisCacheService.get(CODE_PREFIX + to);
            if(StringUtils.isNotBlank(json)){
                //{"mobile":"13750001373","checkCode":"9283","checkNum":0,"createTime":1467953645699}
                MobileCode mobileCode = JSONUtil2.fromJson(json, MobileCode.class);
                //同一个手机验证码只能检查有限次数
                int checkNum = mobileCode.getCheckNum();
                if(checkNum >= MobileCode.CHECK_MAX_NUM){
                    //验证超过最大次数
                    throw new CheckOutMaxTimesException("验证超过最大次数");
                }else{
                    mobileCode.setCheckNum(checkNum +1);
                    boolean flag = vc.equals(mobileCode.getCheckCode());
                    if(flag){
                        return true;
                    }else{
                        throw new InvalidValidateCodeException("验证码错误");
                    }
                }
            }else{
                throw new CheckCodeNotFoundException("验证码不存在或已过期");
            }
        }else{
            throw new InvalidValidateCodeException("手机号或验证码为空");
        }
    }

    /**
     * 生成随机的n位数字
     * @param n
     * @return
     */
    public static String random(int n) {
        if (n < 1 || n > 10) {
            throw new IllegalArgumentException("cannot random " + n + " bit number");
        }
        Random ran = new Random();
        if (n == 1) {
            return String.valueOf(ran.nextInt(10));
        }
        int bitField = 0;
        char[] chs = new char[n];
        for (int i = 0; i < n; i++) {
            while(true) {
                int k = ran.nextInt(10);
                if( (bitField & (1 << k)) == 0) {
                    bitField |= 1 << k;
                    chs[i] = (char)(k + '0');
                    break;
                }
            }
        }
        return new String(chs);
    }

}
