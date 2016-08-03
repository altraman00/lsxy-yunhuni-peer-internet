package com.lsxy.framework.api.exceptions;

/**
 * 账号不存在或账号被锁定会抛出此异常
 * Created by liups on 2016/8/1.
 */
public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}
