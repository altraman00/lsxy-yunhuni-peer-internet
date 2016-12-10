package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class IPNotInWhiteListException extends YunhuniApiException{
    public IPNotInWhiteListException(Throwable t) {
        super(t);
    }

    public IPNotInWhiteListException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.IPNotInWhiteList;
    }
}
