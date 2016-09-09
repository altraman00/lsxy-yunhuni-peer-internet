package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

/**
 * 应用没上线会抛出此异常
 * Created by liups on 2016/9/7.
 */
public class AppOffLineException extends YunhuniApiException {

    public AppOffLineException(Throwable t) {
        super(t);
    }

    public AppOffLineException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AppOffLine;
    }
}
