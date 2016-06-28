package com.lsxy.app.portal.rest.console.account;

import com.lsxy.framework.core.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by zhangxb on 2016/6/27.
 * 安全设置VO对象
 */
public class SafetyVo implements Serializable {

    private static final long serialVersionUID = 1L;
    private String username;//登陆账号
    private String isReal;//是否实名认证
    private String userId;//账号id
    private Date time;// 注册时间
    private String mobile;// 手机号码
    private String isMobile;//是否绑定手机
    private String isEmail;//是否绑定邮箱
    private String isPrivate;//是否设置密保
    private String isPsw;//是否设置密码

    public SafetyVo() {
    }

    public SafetyVo(String isPrivate, String username, String isReal, String userId, Date time, String mobile, String isMobile, String isEmail, String isPsw) {
        this.isPrivate = isPrivate;
        this.username = username;
        this.isReal = isReal;
        this.userId = userId;
        this.time = time;
        this.mobile = mobile;
        this.isMobile = isMobile;
        this.isEmail = isEmail;
        this.isPsw = isPsw;
    }

    public String getIsMobile() {
        return isMobile;
    }

    public void setIsMobile(String isMobile) {
        this.isMobile = isMobile;
    }

    public String getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(String isEmail) {
        this.isEmail = isEmail;
    }

    public String getIsPrivate() {
        return isPrivate;
    }

    public void setIsPrivate(String isPrivate) {
        this.isPrivate = isPrivate;
    }

    public String getMobile() {
        return mobile.substring(0,3)+"****"+mobile.substring(7,11);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUsername() {
        return username;
    }

    public String getIsReal() {
        return isReal;
    }

    public String getUserId() {
        return userId;
    }

    public String getTime() {
        return DateUtils.getTime("yyyy-MM-dd HH:mm:ss");
    }

    public void setIsReal(String isReal) {
        this.isReal = isReal;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
