package com.lsxy.framework.core.exceptions.api;

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
