package com.lsxy.framework.core.exceptions.api;

/**
 * 请求参数错误
 * Created by liups on 2016/9/7.
 */
public class RequestIllegalArgumentException extends YunhuniApiException {

    public RequestIllegalArgumentException(Throwable t) {
        super(t);
    }

    public RequestIllegalArgumentException() {
        super();
    }

    public RequestIllegalArgumentException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.IllegalArgument;
    }
}
