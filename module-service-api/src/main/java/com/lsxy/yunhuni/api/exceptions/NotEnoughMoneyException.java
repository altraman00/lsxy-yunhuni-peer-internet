package com.lsxy.yunhuni.api.exceptions;

/**
 * 当支付时余额不足会抛出此异常
 * Created by liups on 2016/7/16.
 */
public class NotEnoughMoneyException extends RuntimeException{
    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
