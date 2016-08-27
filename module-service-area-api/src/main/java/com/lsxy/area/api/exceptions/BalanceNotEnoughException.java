package com.lsxy.area.api.exceptions;

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

    @Override
    public ApiExceptionEnum getApiExceptionEnum() {
        return ApiExceptionEnum.BalanceNotEnough;
    }
}
