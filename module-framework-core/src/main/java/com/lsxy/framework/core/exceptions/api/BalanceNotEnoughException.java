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

    public BalanceNotEnoughException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.BalanceNotEnough;
    }
}
