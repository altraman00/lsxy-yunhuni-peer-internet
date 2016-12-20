package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ChannelNotExistException extends YunhuniApiException {
    public ChannelNotExistException(Throwable t) {
        super(t);
    }

    public ChannelNotExistException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ChannelNotExist;
    }
}
