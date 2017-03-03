package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/8/23.
 */
public class ConfNotExistsException extends YunhuniApiException {
    public ConfNotExistsException(Throwable t) {
        super(t);
    }

    public ConfNotExistsException() {
        super();
    }

    public ConfNotExistsException(String context) {
        super(context);
    }

    public ConfNotExistsException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ConfNotExists;
    }

}
