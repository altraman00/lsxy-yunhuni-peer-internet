package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

/**
 * Created by liups on 2016/8/23.
 */
public class AppServiceInvalidException extends YunhuniApiException {
    public AppServiceInvalidException(Throwable t) {
        super(t);
    }

    public AppServiceInvalidException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.AppServiceInvalid;
    }

}
