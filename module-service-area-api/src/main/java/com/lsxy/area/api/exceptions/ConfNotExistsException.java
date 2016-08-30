package com.lsxy.area.api.exceptions;

/**
 * Created by liups on 2016/8/23.
 */
public class ConfNotExistsException extends YunhuniApiException {
    public ConfNotExistsException(Throwable t) {
        super(t);
    }

    public ConfNotExistsException() {
        super();
    }

    @Override
    public ApiExceptionEnum getApiExceptionEnum() {
        return ApiExceptionEnum.ConfNotExists;
    }

}
