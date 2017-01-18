package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
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

    private static void call(
                String appId,String confId,String from ,String to,String maxDuration ,
                String maxDialDuration ,String dialVoiceStopCond ,String playFile ,String voiceMode )
        {
            Map<String,Object> params = new HashMap<String,Object>();
            params.put("from",from);
            params.put("to",to);
            params.put("max_duration",maxDuration);
            params.put("max_dial_duration",maxDialDuration);
            params.put("dial_voice_stop_cond",dialVoiceStopCond);
            params.put("play_file",playFile);
            params.put("voice_mode",voiceMode);
            logger.info("[邀请加入会议]参数："+params);
            try {
                String result =  HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CONF_INVITE_CALL,confId),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
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
        logger.info("[开始][max_parts为空]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_parts为空]");
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
        String maxParts = "-10";
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_parts设置-10s]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_parts设置-10s]");
    }
    @Test
    public void test8(){
        String maxDuration = "50";
        String maxParts = "5.5";
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][max_parts设置5.5s]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][max_parts设置5.5s]");
    }
    @Test
    public void test9(){
        String maxDuration = "500";
        String maxParts = null;
        Boolean recording = false;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][recording默认值false]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][recording默认值false]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test9_call(){
        String confId = "8a2a294c59af56040159b021246f0015";
        String from = null;
        String to = phone2;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][recording默认值false]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][recording默认值false]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }


    @Test
    public void test10(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = true;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][recording设置ture]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][recording设置ture]");
    }
    @Test
    public void test11(){
        String maxDuration = "500";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = false;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][auto_hangup默认值false]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][auto_hangup默认值false]");
    }
    @Test
    public void test12(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = true;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][recording设置ture]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][recording设置ture]");
    }
    @Test
    public void test13(){
        String maxDuration = "50";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = null;
        String userData = null;
        logger.info("[开始][bgm_file为空]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][bgm_file为空]");
    }
    @Test
    public void test14(){
        String maxDuration = "500";
        String maxParts = null;
        Boolean recording = null;
        Boolean autoHangup = null;
        String bgmFile = voice_file1;
        String userData = null;
        logger.info("[开始][bgm_file设置]");
        send(  appId, maxDuration, maxParts, recording, autoHangup, bgmFile, userData);
        logger.info("[结束][bgm_file设置]");
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




    @Test
    public void test16(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][from为空]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][from为空]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test17(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = num1;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][from指定存在的号码]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][from指定存在的号码]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test18(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = badPhone;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][from指定不存在的号码]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][from指定不存在的号码]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test19(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = null;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][to为空]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][to为空]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test20(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][to填写被叫号码]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][to填写被叫号码]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test21(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][recording默认值false]");
        logger.info("[开始][目前没有改限制]");
//        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][recording默认值false]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test22(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][同一主叫号码在同一时间呼叫设置呼出次数]");
        logger.info("[开始][目前没有改限制]");
        //call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][同一主叫号码在同一时间呼叫设置呼出次数]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test23(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = null;
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_duration为空]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_duration为空]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test24(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "20";
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_duration设置]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_duration设置]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test25(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "-10";
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_duration设置-10s]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_duration设置-10s]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test26(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "5.5";
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_duration设置5.5s]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_duration设置5.5s]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test27(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = null;
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_dial_duration默认]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_dial_duration默认]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test28(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "10";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_dial_duration设置]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_dial_duration设置]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test29(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "-10";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_dial_duration设置-10s]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_dial_duration设置-10s]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test30(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "5.5";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][max_dial_duration设置5.5s]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][max_dial_duration设置5.5s]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test31(){
        String confId = "8a2d9fed59ab16d80159ab7e73b8000b";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][dial_voice_stop_cond默认]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][dial_voice_stop_cond默认]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test32(){
        String confId = "8a2d9fed59ab16d80159ab7e73b8000b";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = "0";
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][dial_voice_stop_cond设置0]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][dial_voice_stop_cond设置0]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test33(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = "1";
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][dial_voice_stop_cond设置1]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][dial_voice_stop_cond设置1]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test34(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = "3";
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][dial_voice_stop_cond设置其他枚举值]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][dial_voice_stop_cond设置其他枚举值]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test35(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][play_file为空]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][play_file为空]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test36(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = voice_file1;
        String voiceMode = null;
        logger.info("[开始][play_file设置]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][play_file设置]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test37(){
        String confId = "8a2a294c59ab17140159ab5b4fce0001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = null;
        logger.info("[开始][voice_mode默认]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][voice_mode默认]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test38(){
        String confId = "8a2a294c59ab81070159ab836ff00001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = "2";
        logger.info("[开始][voice_mode设置2]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][voice_mode设置2]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test39(){
        String confId = "8a2a294c59ab81070159ab836ff00001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = "3";
        logger.info("[开始][voice_mode设置3]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][voice_mode设置3]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test40(){
        String confId = "8a2a294c59ab81070159ab836ff00001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = "4";
        logger.info("[开始][voice_mode设置4]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][voice_mode设置4]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }
    @Test
    public void test41(){
        String confId = "8a2a294c59ab81070159ab836ff00001";
        String from = null;
        String to = phone1;
        String maxDuration = "50";
        String maxDialDuration = "50";
        String dialVoiceStopCond = null;
        String playFile = null;
        String voiceMode = "5";
        logger.info("[开始][voice_mode设置其他枚举值]");
        call( appId, confId, from , to, maxDuration , maxDialDuration , dialVoiceStopCond , playFile , voiceMode);
        logger.info("[结束][voice_mode设置其他枚举值]");
        //8a2d9fed59ab16d80159ab4cb4f30003
    }

}
