package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ConditionNotExistException extends YunhuniApiException {
    public ConditionNotExistException(Throwable t) {
        super(t);
    }

    public ConditionNotExistException() {
        super();
    }

    public ConditionNotExistException(String context) {
        super(context);
    }

    public ConditionNotExistException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ConditionNotExist;
    }
}
