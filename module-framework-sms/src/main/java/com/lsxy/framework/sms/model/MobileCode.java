package com.lsxy.framework.sms.model;

import org.springframework.util.StringUtils;

/**
 * Created by liups on 2016/6/23.
 */
public class MobileCode {
    public static int CHECK_MAX_NUM = 5;  //验证最大次数
    public static long TIME_INTERVAL = 50000; //发送验证码的最小间隔(前端设置了60秒，这里设置50秒)

    private String mobile;
    private String checkCode;
    private int checkNum = 0;
    private boolean isPass = false;
    private long createTime = System.currentTimeMillis();

    public MobileCode(String mobile, String checkCode) {
        this.mobile = mobile;
        this.checkCode = checkCode;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public int getCheckNum() {
        return checkNum;
    }

    public void setCheckNum(int checkNum) {
        this.checkNum = checkNum;
    }

    public String getCheckCode() {
        return checkCode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public long getCreateTime(){
        return createTime;
    }

}
