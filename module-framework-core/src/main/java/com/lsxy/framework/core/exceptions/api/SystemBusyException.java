package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class SystemBusyException extends YunhuniApiException {
    public SystemBusyException(Throwable t) {
        super(t);
    }

    public SystemBusyException() {
        super();
    }

    public SystemBusyException(String context) {
        super(context);
    }

    public SystemBusyException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.SystemBusy;
    }
}
