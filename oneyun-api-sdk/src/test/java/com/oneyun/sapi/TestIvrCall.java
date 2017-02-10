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
        String from = num1;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][from指定存在的号码]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from指定存在的号码]");
    }
    @Test
    public void test3(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][from1为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][from1为空]");
    }
    @Test
    public void test4(){
        String from = null;
        String to = null;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][to为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][to为空]");
    }
    @Test
    public void test5(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][to填写被叫号码]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][to填写被叫号码]");
    }
    @Test
    public void test6(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][同时重复调用]");
        logger.info("当前没有限制");
        //send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][同时重复调用]");
    }
    @Test
    public void test7(){
        String from = null;
        String to = null;
        String maxDialDuration = null;
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][同一主叫号码在同一时间呼叫设置呼出次数]");
        logger.info("当前没有限制");
        //send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][同一主叫号码在同一时间呼叫设置呼出次数]");
    }
    @Test
    public void test8(){
        String from = null;
        String to = phone2;
        String maxDialDuration = null;
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][max_dial_duration为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_dial_duration为空]");
    }
    @Test
    public void test9(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "10";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][max_dial_duration设置10s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_dial_duration设置10s]");
    }
    @Test
    public void test10(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "-10";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][max_dial_duration设置-10s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_dial_duration设置-10s]");
    }
    @Test
    public void test11(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "5.5";
        String maxCallDuration = "50";
        String userDate = null;
        logger.info("[开始][max_dial_duration设置5.5s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_dial_duration设置5.5s]");
    }
    @Test
    public void test12(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = null;
        String userDate = null;
        logger.info("[开始][max_call_duration为空]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_call_duration为空]");
    }
    @Test
    public void test13(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "10";
        String maxCallDuration = "20";
        String userDate = null;
        logger.info("[开始][max_call_duration设置10s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_call_duration设置10s]");
    }
    @Test
    public void test14(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "10";
        String maxCallDuration = "-10";
        String userDate = null;
        logger.info("[开始][max_call_duration设置-10s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_call_duration设置-10s]");
    }
    @Test
    public void test15(){
        String from = null;
        String to = phone2;
        String maxDialDuration = "50";
        String maxCallDuration = "5.5";
        String userDate = null;
        logger.info("[开始][max_call_duration设置5.5s]");
        send( appId, from, to, maxDialDuration, maxCallDuration, userDate);
        logger.info("[结束][max_call_duration设置5.5s]");
    }
}
