package com.lsxy.app.portal.exceptions;

/**
 * Created by Tandy on 2016/6/25.
 */
public enum  APIErrors {

    LOGIN_ERROR_PWD_MISTAKE("0","登陆失败,用户名密码错误"),
    LOGIN_ERROR("0","登陆失败,未知异常,联系管理员吧");


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
