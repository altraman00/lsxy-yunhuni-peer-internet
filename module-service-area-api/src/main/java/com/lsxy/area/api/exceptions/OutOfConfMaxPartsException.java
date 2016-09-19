package com.lsxy.area.api.exceptions;

import com.lsxy.area.api.ApiReturnCodeEnum;

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

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.OutOfConfMaxParts;
    }
}