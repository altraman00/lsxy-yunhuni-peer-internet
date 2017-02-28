package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class AgentExpiredException extends YunhuniApiException {
    public AgentExpiredException(Throwable t) {
        super(t);
    }

    public AgentExpiredException() {
        super();
    }

    public AgentExpiredException(String context){
        super(context);
    }

    public AgentExpiredException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AgentExpired;
    }
}
