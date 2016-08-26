package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/23.
 */
public class NumberNotAllowToCallException extends YunhuniApiException {
    public NumberNotAllowToCallException(String message) {
        super(message);
    }

    @Override
    public String getCode() {
        return "101013";
    }


}
