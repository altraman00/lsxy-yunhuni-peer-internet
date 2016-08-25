package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/23.
 */
public class BalanceNotEnoughException extends RuntimeException {
    public BalanceNotEnoughException(String message) {
        super(message);
    }
}
