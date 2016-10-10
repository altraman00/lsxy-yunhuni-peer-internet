package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

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
