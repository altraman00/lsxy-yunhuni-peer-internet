package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

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
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.BalanceNotEnough;
    }
}
