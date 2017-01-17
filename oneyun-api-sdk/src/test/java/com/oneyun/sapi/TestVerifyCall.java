package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音验证码
 * Created by zhangxb on 2017/1/16.
 */
public class TestVerifyCall extends BaseTest {
    private static void send(String appId,String from,String to,Integer maxDialDuration,Integer repeat
            ,String[] files,String verifyCode,String userData){
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("verify_code",verifyCode);
        params.put("user_data",userData);
        logger.info("[语音验证码]参数："+params);
        try {
            String result =  sapiSdk.verifyCall( appId, from, to, maxDialDuration, repeat
                    , files, verifyCode, userData);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    private static void send1(String appId,String from,String to,String maxDialDuration,Integer repeat
            ,String[] files,String verifyCode,String userData){
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("verify_code",verifyCode);
        params.put("user_data",userData);
        logger.info("[语音验证码]参数："+params);
        try {
            String result =   HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALL_VERIFY_CALL),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test1(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][from为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][from为空]");
    }
    @Test
    public void test2(){
        String from = num1;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][from指定存在的号码]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][from指定存在的号码]");
    }
    @Test
    public void test3(){
        String from = badPhone;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][from指定不存在的号码]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][from指定不存在的号码]");
    }
    @Test
    public void test4(){
        String from = null;
        String to = null;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][to为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][to为空]");
    }
    @Test
    public void test5(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][to填写被叫号码]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][to填写被叫号码]");
    }
    @Test
    public void test6(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][同时重复调用]");
        for (int i = 0; i < 3; i++) {
            send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        }
        logger.info("[结束][同时重复调用]");
    }
    @Test
    public void test7(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][同一主叫号码在同一时间呼叫设置呼出次数]");
        logger.info("没有这个限制");
        //send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][同一主叫号码在同一时间呼叫设置呼出次数]");
    }
    @Test
    public void test8(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][max_dial_duration为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][max_dial_duration为空]");
    }
    @Test
    public void test9(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 10;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][max_dial_duration设置10s]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][max_dial_duration设置10s]");
    }
    @Test
    public void test10(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = -10;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][max_dial_duration设置-10s]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][max_dial_duration设置-10s]");
    }
    @Test
    public void test11(){
        String from = null;
        String to = phone2;
        String maxDialDuration = 5.5+"";
        Integer repeat = null;
        String[] files = null;
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][max_dial_duration设置5.5s]");
        send1(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][max_dial_duration设置5.5s]");
    }
    @Test
    public void test12(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][repeat为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][repeat为空]");
    }
    @Test
    public void test13(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = 1;
        String[] files = null;
        String verifyCode = "123456";
        String userData = null;
        logger.info("[开始][repeat设置1]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][repeat设置1]");
    }
    @Test
    public void test14(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][play_file为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][play_file为空]");
    }
    @Test
    public void test15(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = {voice_file1};
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][play_file指定应用放音文件]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][play_file指定应用放音文件]");
    }
    @Test
    public void test16(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][play_file、verify_code同时为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][play_file、verify_code同时为空]");
    }
    @Test
    public void test17(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = {voice_file1};
        String verifyCode = null;
        String userData = null;
        logger.info("[开始][verify_code为空]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][verify_code为空]");
    }
    @Test
    public void test18(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "45465";
        String userData = null;
        logger.info("[开始][verify_code正整数]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][verify_code正整数]");
    }
    @Test
    public void test19(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "-100";
        String userData = null;
        logger.info("[开始][verify_code负整数]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][verify_code负整数]");
    }
    @Test
    public void test20(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "10.52";
        String userData = null;
        logger.info("[开始][verify_code小数]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][verify_code小数]");
    }
    @Test
    public void test21(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;
        Integer repeat = null;
        String[] files = null;
        String verifyCode = "1234567891234";
        String userData = null;
        logger.info("[开始][verify_code13个数字]");
        send(  appId, from, to, maxDialDuration, repeat, files, verifyCode, userData);
        logger.info("[结束][verify_code13个数字]");
    }
}
