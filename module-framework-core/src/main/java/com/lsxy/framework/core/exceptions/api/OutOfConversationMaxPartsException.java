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

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.OutOfConversationMaxParts;
    }
}
