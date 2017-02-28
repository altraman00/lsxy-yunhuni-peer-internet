package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class OutOfConversationMaxPartsException extends YunhuniApiException {
    public OutOfConversationMaxPartsException(Throwable t) {
        super(t);
    }

    public OutOfConversationMaxPartsException() {
        super();
    }

    public OutOfConversationMaxPartsException(String context) {
        super(context);
    }

    public OutOfConversationMaxPartsException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.OutOfConversationMaxParts;
    }
}
