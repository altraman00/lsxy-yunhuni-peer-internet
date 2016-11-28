package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liuws on 2016/9/10.
 */
public class PlayFileNotExistsException extends YunhuniApiException {

    public PlayFileNotExistsException(Throwable t) {
        super(t);
    }

    public PlayFileNotExistsException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.PlayFileNotExists;
    }

}