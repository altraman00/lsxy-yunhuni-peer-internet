package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ConversationNotExistException extends YunhuniApiException {
    public ConversationNotExistException(Throwable t) {
        super(t);
    }

    public ConversationNotExistException() {
        super();
    }

    public ConversationNotExistException(String context) {
        super(context);
    }

    public ConversationNotExistException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ConversationNotExist;
    }
}
