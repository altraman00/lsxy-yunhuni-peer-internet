package com.oneyun.sapi;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhangxb on 2017/1/16.
 */
public class BaseTest {
    protected static String url = "http://api.yunhuni.cn/v1/account/07f1a0c8941e8e0c829af95d08956806/";
    protected static String certId = "07f1a0c8941e8e0c829af95d08956806";
    protected static String secretKey = "928f1b3292dd0f2c2063390c43e4dbc4";
    protected static String appId = "8a2bc67257fae1820157faef2f290002";
    protected static SapiSdk sapiSdk = new SapiSdk(url,certId,secretKey);
    protected static MyLogger logger = new MyLogger();
    protected static String num1 = "076087882208";//应用绑定号码
    protected static String num2 = "076087882232";//用户下可用号码
    protected static String voice_file1 = "sendoutgoods.wav";//录音文件
    protected static String phone1 = "18620591870";
    protected static String phone2 = "18826474526";
    protected static String badPhone = "4545646";
    protected static String getArrayString(String[] files, String play_file) {
        if(files!=null) {
            for (int i = 0; i < files.length; i++) {
                if(StringUtils.isNotEmpty(files[i])) {
                    play_file += files[i];
                    if (i !=(files.length - 1)) {
                        play_file += "|";
                    }
                }
            }
        }
        return play_file;
    }
}
