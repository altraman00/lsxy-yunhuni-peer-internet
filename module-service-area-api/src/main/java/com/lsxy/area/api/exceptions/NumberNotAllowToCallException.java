package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

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
