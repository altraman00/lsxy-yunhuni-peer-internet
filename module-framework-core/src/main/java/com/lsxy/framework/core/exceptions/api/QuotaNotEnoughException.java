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

    public QuotaNotEnoughException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.QuotaNotEnough;
    }
}
