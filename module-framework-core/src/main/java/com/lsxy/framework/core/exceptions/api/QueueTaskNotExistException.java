package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liuws on 2017/03/01.
 */
public class QueueTaskNotExistException extends YunhuniApiException {
    public QueueTaskNotExistException(Throwable t) {
        super(t);
    }

    public QueueTaskNotExistException() {
        super();
    }

    public QueueTaskNotExistException(String context) {
        super(context);
    }

    public QueueTaskNotExistException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.QueueTaskNotExist;
    }
}
