package com.lsxy.framework.core.exceptions.api;

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

    public AppOffLineException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AppOffLine;
    }
}
