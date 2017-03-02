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

    public RequestIllegalArgumentException(String context) {
        super(context);
    }

    public RequestIllegalArgumentException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.IllegalArgument;
    }
}
