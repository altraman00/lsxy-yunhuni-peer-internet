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

    public CallNotExistsException(String context) {
        super(context);
    }

    public CallNotExistsException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.CallNotExists;
    }

}
