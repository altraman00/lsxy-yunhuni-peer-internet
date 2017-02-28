package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class AppServiceInvalidException extends YunhuniApiException {
    public AppServiceInvalidException(Throwable t) {
        super(t);
    }

    public AppServiceInvalidException() {
        super();
    }

    public AppServiceInvalidException(String context) {
        super(context);
    }

    public AppServiceInvalidException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AppServiceInvalid;
    }

}
