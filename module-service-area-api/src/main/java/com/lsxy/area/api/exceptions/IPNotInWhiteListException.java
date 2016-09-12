package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

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
