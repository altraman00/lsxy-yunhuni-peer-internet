package com.lsxy.app.portal.rest.console.account;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

import static javafx.scene.input.KeyCode.T;
import static jdk.nashorn.internal.objects.NativeString.substring;

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
    private static final String IS_TRUE = "1";//已设置
    private static final String IS_FALSE = "-1";//未设置

    private String isTrue(String temp){
        if(temp==null||temp.length()==0){
            return IS_FALSE;
        }else{
            return  IS_TRUE;
        }
    }
    public SafetyVo() {
    }

    public  SafetyVo(Account account){
        this.isPrivate = IS_FALSE;
        this.username = account.getUserName();
        Tenant tenant =account.getTenant();
        if(tenant!=null) {
            this.isReal =tenant.getIsRealAuth()==0 ? IS_TRUE : IS_FALSE;
        }else{
            this.isReal=IS_FALSE;
        }
        this.userId = account.getId();
        this.time = account.getCreateTime();
        this.isMobile = isTrue(account.getMobile());
        this.mobile = this.isMobile==IS_TRUE?account.getMobile().substring(0,3)+"****"+account.getMobile().substring(7,11):"";
        this.isEmail = isTrue(account.getEmail());
        this.isPsw = IS_TRUE;
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
        return mobile;
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
