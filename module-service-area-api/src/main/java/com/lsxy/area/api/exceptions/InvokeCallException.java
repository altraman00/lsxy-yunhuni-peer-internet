package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

/**
 * Created by tandy on 16/8/18.
 */
public class InvokeCallException extends YunhuniApiException {
    public InvokeCallException(Throwable t) {
        super(t);
    }

    public InvokeCallException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.InvokeCall;
    }
}
