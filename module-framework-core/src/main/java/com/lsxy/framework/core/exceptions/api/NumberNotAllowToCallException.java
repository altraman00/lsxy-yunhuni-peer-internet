package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class NumberNotAllowToCallException extends YunhuniApiException {
    public NumberNotAllowToCallException(Throwable t) {
        super(t);
    }

    public NumberNotAllowToCallException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.NumberNotAllowToCall;
    }

}
