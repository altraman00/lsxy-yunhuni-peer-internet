package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ExtensionBindingToAgentException extends YunhuniApiException {
    public ExtensionBindingToAgentException(Throwable t) {
        super(t);
    }

    public ExtensionBindingToAgentException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ExtensionBindingToAgent;
    }
}
