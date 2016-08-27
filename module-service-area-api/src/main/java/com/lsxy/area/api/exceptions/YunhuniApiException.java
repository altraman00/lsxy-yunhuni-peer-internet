package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/26.
 */
public abstract class YunhuniApiException extends Exception{
    public YunhuniApiException(Throwable t) {
        super(t);
    }

    public YunhuniApiException() {
        super();
    }

    public abstract ApiExceptionEnum getApiExceptionEnum();

    public final String getCode(){
        return getApiExceptionEnum().getCode();
    }

    public final String getMessage(){
        return getApiExceptionEnum().getMsg();
    }
}
