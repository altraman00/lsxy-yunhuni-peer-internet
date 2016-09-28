package com.lsxy.framework.sms.service;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.sms.exceptions.CheckCodeNotFoundException;
import com.lsxy.framework.sms.exceptions.CheckOutMaxTimesException;
import com.lsxy.framework.sms.exceptions.InvalidValidateCodeException;
import com.lsxy.framework.sms.exceptions.TooManyGenTimesException;
import com.lsxy.framework.sms.model.MobileCode;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

/**
 * Created by Tandy on 2016/7/7.
 */
public abstract class AbstractSmsServiceImpl implements  SmsService{

    public abstract RedisCacheService getRedisCacheService();

    /**
     * 根据制定模板构建短信内容
     * @param template
     * @param params
     * @return
     */
    protected String buildSmsContent(String template, Map<String, Object> params) {
        Properties properties = new Properties();
        properties.setProperty("resource.loader", "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        VelocityEngine velocityEngine = new VelocityEngine(properties);
        VelocityContext context = new VelocityContext();
        StringWriter writer = new StringWriter();
        for(String key : params.keySet()){
            context.put(key, params.get(key));
        }
        velocityEngine.mergeTemplate("/templates/" + template, "utf-8", context, writer);
        return writer.toString();
    }


    @Override
    public String genVC(String to) throws TooManyGenTimesException {
        String random = random(Integer.parseInt(SystemConfig.getProperty("global.sms.random.num",4+"")));
        long codeSameTime = System.currentTimeMillis() + MobileCode.CODE_SAME_TIME;
        String getJson = getRedisCacheService().get(CODE_PREFIX + to);
        if(StringUtils.isNotBlank(getJson)){
            MobileCode getCode = JSONUtil2.fromJson(getJson, MobileCode.class);
            if(getCode != null){
                if(System.currentTimeMillis() < (getCode.getCreateTime() + MobileCode.TIME_INTERVAL)){
                    throw new TooManyGenTimesException("验证码生成过于频繁");
                }else{
                    if(System.currentTimeMillis() < getCode.getCodeSameTime()){
                        random = getCode.getCheckCode();
                        codeSameTime = getCode.getCodeSameTime();
                    }
                }
            }
        }
        MobileCode mobileCode = new MobileCode(to,random,codeSameTime);
        String setJson = JSONUtil2.objectToJson(mobileCode);
        getRedisCacheService().set(CODE_PREFIX + to,setJson , Long.parseLong(SystemConfig.getProperty("global.sms.code.expire",1800+"")));
        return random;
    }

    @Override
    public boolean checkVC(String to, String vc) throws InvalidValidateCodeException, CheckOutMaxTimesException, CheckCodeNotFoundException {
        if(StringUtils.isNotBlank(to) && StringUtils.isNotBlank(vc)){
            String json = getRedisCacheService().get(CODE_PREFIX + to);
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
    public String random(int n) {
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
