package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentHasAlreadyLoggedInException extends YunhuniApiException {
    public AgentHasAlreadyLoggedInException(Throwable t) {
        super(t);
    }

    public AgentHasAlreadyLoggedInException() {
        super();
    }

    public AgentHasAlreadyLoggedInException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentHasAlreadyLoggedIn;
    }
}
