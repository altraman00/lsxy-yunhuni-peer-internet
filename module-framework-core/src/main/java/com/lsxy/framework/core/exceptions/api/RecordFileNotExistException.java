package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class RecordFileNotExistException extends YunhuniApiException {
    public RecordFileNotExistException(Throwable t) {
        super(t);
    }

    public RecordFileNotExistException() {
        super();
    }

    public RecordFileNotExistException(String context) {
        super(context);
    }

    public RecordFileNotExistException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.RecordFileNotExists;
    }
}
