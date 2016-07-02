package com.lsxy.yuhuni.api.exceptions;

/**
 * 当修改鉴权账号（凭证）的secretKey次数达到当天上限时，抛出此异常
 * Created by liups on 2016/6/30.
 */
public class SkChangeCountLimitException extends RuntimeException{
    public SkChangeCountLimitException(String message) {
        super(message);
    }
}
