package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 会议测试
 * Created by zhangxb on 2017/1/17.
 */
public class TestConf extends BaseTest{
    private static void send(String appId,String maxDuration,String maxParts,Boolean recording,Boolean autoHangup,String bgmFile,String userData){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("max_duration",maxDuration);
        params.put("max_parts",maxParts);
        params.put("recording",recording);
        params.put("auto_hangup",autoHangup);
        params.put("bgm_file",bgmFile);
        params.put("user_data",userData);
        logger.info("[创建会议]参数："+params);
        try {
            String result =  HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CONF_CREATE),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test1(){
        String maxDuration = null;
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration为空]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration为空]");
    }
    @Test
    public void test2(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test3(){
        String maxDuration = "-10";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置-10s]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置-10s]");
    }
    @Test
    public void test4(){
        String maxDuration = "5.5";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置5.5s]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置5.5s]");
    }
    @Test
    public void test5(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test6(){
        String maxDuration = "50";
        String maxParts = "5";
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test7(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test8(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test9(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test10(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test11(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test12(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test13(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test14(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
    @Test
    public void test15(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_duration设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_duration设置]");
    }
}
