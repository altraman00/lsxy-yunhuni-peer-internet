package com.lsxy.app.portal.rest.comm;

import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * Created by liups on 2016/6/23.
 */
public class MobileCodeChecker {
    public static byte STATUS_ERR_CODE = 0;  //验证码错误
    public static byte STATUS_PASS = 1;      //验证通过
    public static byte STATUS_OVER_NUM = 2;  //验证达到最大次数
    public static byte STATUS_OVER_TIME = 3; //验证码过期

    public static int CHECK_MAX_NUM = 5;  //验证最大次数

    public static long TIME_INTERVAL = 50000; //发送验证码的最小间隔(前端设置了60秒，这里设置50秒)

    private String mobile;
    private String checkCode;
    private int checkNum = 0;
    private boolean isPass = false;
    private long createTime = System.currentTimeMillis();

    public MobileCodeChecker(String mobile, String checkCode) {
        this.mobile = mobile;
        this.checkCode = checkCode;
    }

    //检查手机验证码
    public static byte checkCode(String code,MobileCodeChecker checker){
        if(!StringUtils.isEmpty(code)){
            //同一个手机验证码只能检查有限次数
            int checkNum = checker.getCheckNum();
            if(checkNum >= MobileCodeChecker.CHECK_MAX_NUM){
                //验证超过最大次数
                return MobileCodeChecker.STATUS_OVER_NUM;
            }else{
                checker.setCheckNum(checkNum +1);
                boolean flag = code.equals(checker.getCheckCode());
                checker.setPass(flag);
                if(flag){
                    return MobileCodeChecker.STATUS_PASS;
                }else{
                    return MobileCodeChecker.STATUS_ERR_CODE;
                }
            }
        }else{
            return MobileCodeChecker.STATUS_ERR_CODE;
        }
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
