package com.lsxy.app.portal.comm;

/**
 * Created by liups on 2016/7/7.
 */
public class MobileCodeChecker {
    private String mobile;
    private boolean isPass;

    public MobileCodeChecker() {
    }

    public MobileCodeChecker(String mobile, boolean isPass) {
        this.mobile = mobile;
        this.isPass = isPass;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }
}
