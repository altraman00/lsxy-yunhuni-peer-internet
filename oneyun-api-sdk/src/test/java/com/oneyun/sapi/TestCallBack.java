package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 双向回拨测试
 * Created by zhangxb on 2017/1/16.
 */
public class TestCallBack extends BaseTest{

    private static void send(String appId,String from1,String to1,String from2,String to2,String ringTone,Integer ringToneMode,
                             Integer maxDialDuration,Integer maxCallDuration,Boolean recording,Integer recordMode,String userData){
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from1",from1);
        params.put("to1",to1);
        params.put("from2",from2);
        params.put("to2",to2);
        params.put("ring_tone",ringTone);
        params.put("ring_tone_mode",ringToneMode);
        params.put("max_dial_duration",maxDialDuration);
        params.put("max_call_duration",maxCallDuration);
        params.put("recording",recording);
        params.put("record_mode",recordMode);
        params.put("user_data",userData);
        logger.info("[双向回拨]参数："+params);
        try {
            String result =  sapiSdk.duoCallback(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    private static void send1(String appId,String from1,String to1,String from2,String to2,String ringTone,Integer ringToneMode,
                             String maxDialDuration,String maxCallDuration,Boolean recording,Integer recordMode,String userData) {

        try {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("from1", from1);
            params.put("to1", to1);
            params.put("from2", from2);
            params.put("to2", to2);
            params.put("ring_tone", ringTone);
            params.put("ring_tone_mode", ringToneMode);
            params.put("max_dial_duration", maxDialDuration);
            params.put("max_call_duration", maxCallDuration);
            params.put("recording", recording);
            params.put("record_mode", recordMode);
            params.put("user_data", userData);
            logger.info("[双向回拨]参数："+params);
            String result =  HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALL_DUO_CALLBACK), sapiSdk.getSecretKey(), sapiSdk.getCertId(), appId, params);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    @Test
    public void test1(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from1为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from1为空]");
    }
    //from1指定存在的号码
    @Test
    public void test2(){
        String from1 = num1;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from1指定存在的号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from1指定存在的号码]");
    }
    @Test
    public void test3(){
        String from1 = badPhone;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from1指定不存在的号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from1指定不存在的号码]");
    }
    @Test
    public void test4(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = null;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone1;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from1为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from1为空]");
    }
    //from1指定存在的号码
    @Test
    public void test5(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][to1填写被叫号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][to1填写被叫号码]");
    }
    @Test
    public void test6(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from2为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from2为空]");
    }
    @Test
    public void test7(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = num2;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from2指定存在的号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from2指定存在的号码]");
    }
    //from1指定存在的号码
    @Test
    public void test8(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = badPhone;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][from2指定不存在的号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][from2指定不存在的号码]");
    }
    @Test
    public void test9(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = "18620591870";//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = null;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][to2为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][to2为空]");
    }
    @Test
    public void test10(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][to2填写被叫号码]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][to2填写被叫号码]");
    }
    //from1指定存在的号码
    @Test
    public void test11(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][同时重复调用]");
        for(int i=0;i<3;i++) {
            send(appId, from1, to1, from2, to2, ringTone, ringToneMode, maxDialDuration, maxCallDuration, recording, recordMode, userData);
        }
        logger.info("[结束][同时重复调用]");
    }
    @Test
    public void test12(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][to1、to2号码相同]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][to1、to2号码相同]");
    }
    @Test
    public void test13(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][同一主叫号码在同一时间呼叫设置呼出次数]--目前无此限制");
        //send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][同一主叫号码在同一时间呼叫设置呼出次数]--目前无此限制");
    }
    //from1指定存在的号码
    @Test
    public void test14(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = 0;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone为空，ring_tone_mode为0]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone为空，ring_tone_mode为0]");
    }
    @Test
    public void test15(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = null;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone1;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = 1;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone为空，ring_tone_mode为1]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone为空，ring_tone_mode为1]");
    }
    @Test
    public void test16(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = 2;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone为空，ring_tone_mode为2]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone为空，ring_tone_mode为2]");
    }
    //from1指定存在的号码
    @Test
    public void test17(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = 3;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone为空，ring_tone_mode为3]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone为空，ring_tone_mode为3]");
    }
    @Test
    public void test18(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone2;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = "17620010850";//必填 第二方的被叫号码。
        String ringTone = voice_file1;//自定义回铃音 选填
        Integer ringToneMode = 0;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone指定放音文件，ring_tone_mode为0]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone指定放音文件，ring_tone_mode为0]");
    }
    @Test
    public void test19(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone2;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = "17620010850";//必填 第二方的被叫号码。
        String ringTone = voice_file1;//自定义回铃音 选填
        Integer ringToneMode = 1;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone指定放音文件，ring_tone_mode为1]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone指定放音文件，ring_tone_mode为1]");
    }
    //from1指定存在的号码
    @Test
    public void test20(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone2;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = "17620010850";//必填 第二方的被叫号码。
        String ringTone = voice_file1;//自定义回铃音 选填
        Integer ringToneMode = 2;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone指定放音文件，ring_tone_mode为2]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone指定放音文件，ring_tone_mode为2]");
    }
    @Test
    public void test21(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone2;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = "17620010850";//必填 第二方的被叫号码。
        String ringTone = voice_file1;//自定义回铃音 选填
        Integer ringToneMode = 3;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone指定放音文件，ring_tone_mode为3]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone指定放音文件，ring_tone_mode为3]");
    }
    @Test
    public void test22(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = voice_file1;//自定义回铃音 选填
        Integer ringToneMode = 4;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][ring_tone指定放音文件，ring_tone_mode为其他枚举值]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][ring_tone指定放音文件，ring_tone_mode为其他枚举值]");
    }
    @Test
    public void test23(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone2;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone1;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = null;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_dial_duration为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_dial_duration为空]");
    }
    @Test
    public void test24(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = null;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone1;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 10;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_dial_duration设置10s]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_dial_duration设置10s]");
    }
    @Test
    public void test25(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = -10;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 50;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_dial_duration设置-10s]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_dial_duration设置-10s]");
    }
    //from1指定存在的号码
    @Test
    public void test26(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        String maxDialDuration = 5.5+"";//必填，最大拨号等待时长（秒）
        String maxCallDuration = 50+"";//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_dial_duration设置5.5s]");
        send1(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_dial_duration设置5.5s]");
    }
    @Test
    public void test27(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = null;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_call_duration为空]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_call_duration为空]");
    }
    @Test
    public void test28(){//18620591870  //18826474526
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = 10;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_call_duration设置10s]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_call_duration设置10s]");
    }
    //from1指定存在的号码
    @Test
    public void test29(){//076087882208 //076087882232
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        Integer maxDialDuration = 50;//必填，最大拨号等待时长（秒）
        Integer maxCallDuration = -10;//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_call_duration设置-10s]");
        send(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_call_duration设置-10s]");
    }
    @Test
    public void test30(){
        String from1 = null;//选填， 向第一方显示的来电号码
        String to1 = phone1;//必填 第一方的被叫号码
        String from2 = null;//选填，  向第二方显示的来电号码
        String to2 = phone2;//必填 第二方的被叫号码。
        String ringTone = null;//自定义回铃音 选填
        Integer ringToneMode = null;//选填，自定义回铃音播放模式.
        String maxDialDuration = 50+"";//必填，最大拨号等待时长（秒）
        String maxCallDuration = 5.5+"";//必填 最大通话时长（秒）
        Boolean recording = null;//是否录音 选填
        Integer recordMode = null;//录音模式： 0: 双向接通后录音 1：开始呼叫第一方时启动录音 2: 开始呼叫第二方时启动录音 选填
        String userData = null;//
        logger.info("[开始][max_call_duration设置5.5s]");
        send1(appId, from1, to1, from2, to2, ringTone, ringToneMode,maxDialDuration, maxCallDuration, recording, recordMode, userData);
        logger.info("[结束][max_call_duration设置5.5s]");
    }
}
