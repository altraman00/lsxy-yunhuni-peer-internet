package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/10/10.
 */
public class AppNotFoundException extends YunhuniApiException{
    public AppNotFoundException(Throwable t){
        super(t);
    }

    public AppNotFoundException(){
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AppNotFound;
    }
}