package com.lsxy.framework.core.exceptions.api;

/**
 * Created by liups on 2016/11/14.
 */
public class UserNumberHasNotAvailableLineException extends YunhuniApiException {
    public UserNumberHasNotAvailableLineException(Throwable t) {
        super(t);
    }

    public UserNumberHasNotAvailableLineException() {
        super();
    }

    public UserNumberHasNotAvailableLineException(String cause) {
        super(cause);
    }

    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.UserNumberHasNotAvailableLine;
    }
}
