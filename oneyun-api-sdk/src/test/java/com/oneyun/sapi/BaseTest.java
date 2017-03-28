package com.oneyun.sapi;

import org.apache.commons.lang.StringUtils;

/**
 * Created by zhangxb on 2017/1/16.
 */
public class BaseTest {
    protected static String url = "https://api.yunhuni.com/v1/account/aadd5c85cb92983ce2f05ac44016a7c5/";
    protected static String certId = "aadd5c85cb92983ce2f05ac44016a7c5";
    protected static String secretKey = "cdd4ff7760dc06cf2f806684bdfbff14";
    protected static String appId = "8af48114594439ad0159453284b8001e";
    protected static SapiSdk sapiSdk = new SapiSdk(url,certId,secretKey);
    protected static MyLogger logger = new MyLogger();
    protected static String num1 = "076087882208";//应用绑定号码
    protected static String num2 = "076087882232";//用户下可用号码
    protected static String voice_file1 = "sendoutgoods.wav";//录音文件
    protected static String phone1 = "13971068693";
    protected static String phone2 = "13971068693";
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
