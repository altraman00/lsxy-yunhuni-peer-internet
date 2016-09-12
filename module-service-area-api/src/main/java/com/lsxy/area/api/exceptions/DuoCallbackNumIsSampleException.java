package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

/**
 * Created by liups on 2016/9/8.
 */
public class DuoCallbackNumIsSampleException extends YunhuniApiException {
    public DuoCallbackNumIsSampleException(Throwable t) {
        super(t);
    }

    public DuoCallbackNumIsSampleException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.DuoCallbackNumIsSample;
    }
}
