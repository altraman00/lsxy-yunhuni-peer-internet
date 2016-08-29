package com.lsxy.area.api.exceptions;

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
    public ApiExceptionEnum getApiExceptionEnum() {
        return ApiExceptionEnum.IPNotInWhiteList;
    }
}
