package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentIsBusyException extends YunhuniApiException {
    public AgentIsBusyException(Throwable t) {
        super(t);
    }

    public AgentIsBusyException() {
        super();
    }

    public AgentIsBusyException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentIsBusy;
    }
}
