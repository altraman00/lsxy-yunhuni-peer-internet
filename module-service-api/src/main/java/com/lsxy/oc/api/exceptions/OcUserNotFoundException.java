package com.lsxy.oc.api.exceptions;

/**
 * 账号不存在或账号被锁定会抛出此异常
 * Created by liups on 2016/8/9.
 */
public class OcUserNotFoundException extends RuntimeException{
    public OcUserNotFoundException(String message){
        super(message);
    }
}
