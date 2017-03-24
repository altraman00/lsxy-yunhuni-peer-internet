package com.lsxy.framework.core.exceptions.api.msg;

import com.lsxy.framework.core.exceptions.api.ApiReturnCodeEnum;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;

/**
 * Created by liups on 2017/3/16.
 */
public class MsgTemplateArgsErrorException extends YunhuniApiException {
    @Override
    public ApiReturnCodeEnum getApiExceptionEnum() {
        return ApiReturnCodeEnum.MsgTemplateArgsError;
    }
}
