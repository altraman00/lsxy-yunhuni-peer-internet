package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class ChannelCanNotDeleteException extends YunhuniApiException {
    public ChannelCanNotDeleteException(Throwable t) {
        super(t);
    }

    public ChannelCanNotDeleteException() {
        super();
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.ChannelCanNotDelete;
    }
}
