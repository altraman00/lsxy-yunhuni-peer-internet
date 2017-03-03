package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2017/2/23.
 */
public class QuotaNotEnoughException extends YunhuniApiException{
    public QuotaNotEnoughException(Throwable t) {
        super(t);
    }

    public QuotaNotEnoughException() {
        super();
    }

    public QuotaNotEnoughException(String context) {
        super(context);
    }

    public QuotaNotEnoughException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.QuotaNotEnough;
    }
}
