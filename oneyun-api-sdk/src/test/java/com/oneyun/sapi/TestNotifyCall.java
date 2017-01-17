package com.oneyun.sapi;

import com.oneyun.sapi.config.SapiConstants;
import com.oneyun.sapi.utils.HttpClientUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 语音通知测试
 * Created by zhangxb on 2017/1/16.
 */
public class TestNotifyCall extends BaseTest{
    private static void send(String appId,String from,String to,Integer maxDialDuration,Integer repeat,String[] files,
                             String[][] playContent, String userData){
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("play_content",playContent);
        params.put("user_data",userData);
        logger.info("[语音通知]参数："+params);
        try {
            String result =  sapiSdk.notifyCall(appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }
    private static void send1(String appId,String from,String to,String maxDialDuration,Integer repeat,String[] files,
                             String[][] playContent, String userData){
        String play_file = "";
        play_file = getArrayString(files, play_file);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("from",from);
        params.put("to",to);
        params.put("max_dial_duration",maxDialDuration);
        params.put("repeat",repeat);
        params.put("play_file",play_file);
        params.put("play_content",playContent);
        params.put("user_data",userData);
        logger.info("[语音通知]参数："+params);
        try {
            String  result = HttpClientUtils.doPost(sapiSdk.getUrl(SapiConstants.CALL_NOTIFY_CALL),sapiSdk.getSecretKey(),sapiSdk.getCertId(),appId,params);
            logger.info("[调用结果]"+result);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }
    }


    @Test
    public void test1(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][from为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][from为空]");
    }
    @Test
    public void test2(){
        String from = num1;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][from指定存在的号码]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][from指定存在的号码]");
    }
    @Test
    public void test3(){
        String from = badPhone;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][from指定不存在的号码]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][from指定不存在的号码]");
    }
    @Test
    public void test4(){
        String from = null;
        String to = null;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][to为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][to为空]");
    }
    @Test
    public void test5(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][to填写被叫号码]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][to填写被叫号码]");
    }
    @Test
    public void test6(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][同时重复调用]");
        for (int i = 0; i < 3 ; i++) {
            send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        }
        logger.info("[结束][同时重复调用]");
    }
    @Test
    public void test7(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][同一主叫号码在同一时间呼叫设置呼出次数]");
        logger.info("没有这个限制");
       // send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][同一主叫号码在同一时间呼叫设置呼出次数]");
    }
    @Test
    public void test8(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = null;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][max_dial_duration为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][max_dial_duration为空]");
    }
    @Test
    public void test9(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 10;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][max_dial_duration设置10s]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][max_dial_duration设置10s]");
    }
    @Test
    public void test10(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = -10;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][max_dial_duration设置-10s]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][max_dial_duration设置-10s]");
    }
    @Test
    public void test11(){
        String from = null;
        String to = phone1;
        String maxDialDuration = 50+"";//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][max_dial_duration设置5.5s]");
        send1( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][max_dial_duration设置5.5s]");
    }
    @Test
    public void test12(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][repeat为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][repeat为空]");
    }
    @Test
    public void test13(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = 1;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][repeat设置1]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][repeat设置1]");
    }
    @Test
    public void test14(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = 1;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_file为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_file为空]");
    }
    @Test
    public void test15(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_file指定应用放音文件]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_file指定应用放音文件]");
    }
    @Test
    public void test16(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_file、play_content同时为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_file、play_content同时为空]");
    }
    @Test
    public void test17(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content为空]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content为空]");
    }
    @Test
    public void test18(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"0",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置0]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置0]");
    }
    @Test
    public void test19(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"100","1",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置1]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置1]");
    }
    @Test
    public void test20(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"50.82","2",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置2]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置2]");
    }
    @Test
    public void test21(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"50.55","3",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置3]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置3]");
    }
    @Test
    public void test22(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"2015-10-10 10:10:10","4",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置4]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置4]");
    }
    @Test
    public void test23(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"01:23:35","5",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置5]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置5]");
    }
    @Test
    public void test24(){
        String from = null;
        String to = phone2;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {"5.5","6",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置6]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置6]");
    }
    @Test
    public void test25(){
        String from = null;
        String to = phone1;
        Integer maxDialDuration = 50;//最大拨号等待时间（秒），默认50秒 选填
        Integer repeat = null;//重复播放次数，默认1，表示播放两次。注意，0表示播放一次，不重复 选填
        String[] files = {};//提示音文件，文件名或者文件名列表，文件名为开发者用户中心的放音文件名 选填
        String[][] playContent = {
                {voice_file1,"7",""}
        }; //动态播放内容，可以是数字、日期、金额 选填
        String userData = null;
        logger.info("[开始][play_content设置其他枚举值]");
        send( appId, from, to, maxDialDuration, repeat, files, playContent,  userData);
        logger.info("[结束][play_content设置其他枚举值]");
    }

}
