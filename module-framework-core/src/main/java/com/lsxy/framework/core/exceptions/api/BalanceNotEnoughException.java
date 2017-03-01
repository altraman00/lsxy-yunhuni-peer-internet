package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class BalanceNotEnoughException extends YunhuniApiException {
    public BalanceNotEnoughException(Throwable t) {
        super(t);
    }

    public BalanceNotEnoughException() {
        super();
    }

    public BalanceNotEnoughException(String context) {
        super(context);
    }

    public BalanceNotEnoughException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.BalanceNotEnough;
    }
}
