package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ExtensionUserExistException extends YunhuniApiException {
    public ExtensionUserExistException(Throwable t) {
        super(t);
    }

    public ExtensionUserExistException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ExtensionUserExist;
    }
}
