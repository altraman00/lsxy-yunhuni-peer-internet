package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ExtensionNotExistException extends YunhuniApiException {
    public ExtensionNotExistException(Throwable t) {
        super(t);
    }

    public ExtensionNotExistException() {
        super();
    }

    public ExtensionNotExistException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ExtensionNotExist;
    }
}
