package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

/**
 * 请求参数错误
 * Created by liups on 2016/9/7.
 */
public class RequestIllegalArgumentException extends YunhuniApiException {

    public RequestIllegalArgumentException(Throwable t) {
        super(t);
    }

    public RequestIllegalArgumentException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.IllegalArgument;
    }
}
