package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * IVR呼叫测试
 * Created by zhangxb on 2017/1/17.
 */
public class TestIvrCall extends BaseTest{
    private static void send(String appId,String from,String to,String maxDialDuration,String maxCallDuration,String userDate){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("max_call_duration",maxCallDuration);
        params.put("user_data",userDate);
        logger.info("[IVR呼叫]参数："+params);
        try {
            String result =  HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.IVR_CALL),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test1(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][from为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from为空]");
    }
    @Test
    public void test2(){
        String from = null;
        String to = phone2;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from指定存在的号码]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from指定存在的号码]");
    }
    @Test
    public void test3(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test4(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test5(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test6(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test7(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test8(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test9(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test10(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test11(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test12(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test13(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test14(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test15(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
}
