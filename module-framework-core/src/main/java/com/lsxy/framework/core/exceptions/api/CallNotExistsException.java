package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class CallNotExistsException extends YunhuniApiException {
    public CallNotExistsException(Throwable t) {
        super(t);
    }

    public CallNotExistsException() {
        super();
    }

    public CallNotExistsException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.CallNotExists;
    }

}
