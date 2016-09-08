package com.lsxy.app.portal.comm;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/7.
 * 由于该类需要放入session
 * 引入springsession后,对象将会存入redis
 * 由于使用了默认对象序列化机制,必须实现Serializable接口,否则会出现序列化异常
 */
public class MobileCodeChecker implements Serializable {
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
