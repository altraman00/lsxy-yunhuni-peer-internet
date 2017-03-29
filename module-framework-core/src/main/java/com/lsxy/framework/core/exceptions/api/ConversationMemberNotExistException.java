package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ConversationMemberNotExistException extends YunhuniApiException {
    public ConversationMemberNotExistException(Throwable t) {
        super(t);
    }

    public ConversationMemberNotExistException() {
        super();
    }

    public ConversationMemberNotExistException(String context) {
        super(context);
    }

    public ConversationMemberNotExistException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ConversationMemberNotExist;
    }
}
