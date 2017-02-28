package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class NotAvailableLineException extends YunhuniApiException {
    public NotAvailableLineException(Throwable t) {
        super(t);
    }

    public NotAvailableLineException() {
        super();
    }

    public NotAvailableLineException(String context) {
        super(context);
    }

    public NotAvailableLineException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.NotAvailableLine;
    }
}
