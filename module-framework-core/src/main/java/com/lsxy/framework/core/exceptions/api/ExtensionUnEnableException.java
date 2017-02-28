package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ExtensionUnEnableException extends YunhuniApiException {
    public ExtensionUnEnableException(Throwable t) {
        super(t);
    }

    public ExtensionUnEnableException() {
        super();
    }

    public ExtensionUnEnableException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ExtensionUnEnable;
    }
}
