package com.lsxy.yunhuni.api.exceptions;

/**
 * 号码被占用异常，当应用上线时，绑定的ivr号码已被占用，则抛出此异常
 * Created by liups on 2016/7/18.
 */
public class TeleNumberBeOccupiedException extends RuntimeException{
    public TeleNumberBeOccupiedException(String message) {
        super(message);
    }
}
