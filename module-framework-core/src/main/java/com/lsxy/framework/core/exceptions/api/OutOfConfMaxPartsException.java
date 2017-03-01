package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liuws on 2016/9/14.
 */
public class OutOfConfMaxPartsException extends YunhuniApiException {

    public OutOfConfMaxPartsException(Throwable t) {
        super(t);
    }

    public OutOfConfMaxPartsException() {
        super();
    }

    public OutOfConfMaxPartsException(String context) {
        super(context);
    }

    public OutOfConfMaxPartsException(ExceptionContext context){
        super(context);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.OutOfConfMaxParts;
    }
}