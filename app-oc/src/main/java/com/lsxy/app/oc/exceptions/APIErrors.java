package com.lsxy.app.oc.exceptions;

/**
 * Created by Tandy on 2016/6/25.
 */
public enum  APIErrors {

    LOGIN_ERROR_PWD_MISTAKE("0","登录失败,密码错误"),
    LOGIN_ERROR_USER_NOT_FOUND("0","登录失败,用户不存在或者账号被锁定"),
    LOGIN_ERROR("0","登录失败,未知异常,联系管理员吧" );


    private APIErrors(String code,String msg){
        this.errorCode = code;
        this.errorMsg = msg;
    }

    private String errorCode;
    private String errorMsg;
    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
