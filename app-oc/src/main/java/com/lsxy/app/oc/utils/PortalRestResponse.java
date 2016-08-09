package com.lsxy.app.oc.utils;

import com.lsxy.app.oc.exceptions.APIErrors;
import com.lsxy.framework.web.rest.RestResponse;

/**
 * Created by Tandy on 2016/6/25.
 */
public class PortalRestResponse<T> extends RestResponse<T>{
    /**
     * 提供失败对象的静态方法，方便输出
     * @param error  错误码
     * @return
     */
    public static RestResponse failed(APIErrors error){
        RestResponse restResponse = new RestResponse();
        restResponse.setSuccess(false);
        restResponse.setErrorCode(error.getErrorCode());
        restResponse.setErrorMsg(error.getErrorMsg());
        return restResponse;
    }
}
