package com.lsxy.app.api.gateway.rest;

import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.area.api.ApiReturnCodeEnum;
import com.lsxy.area.api.exceptions.YunhuniApiException;
import com.lsxy.framework.core.utils.JSONUtil2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tandy on 2016/6/28.
 */
@RequestMapping("/${api.gateway.version}/account/")
public class AbstractAPIController {
    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public String exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        response.setContentType("application/json;charset=UTF-8");
        ex.printStackTrace();
        ApiGatewayResponse failed;
        if(ex instanceof YunhuniApiException){
            failed = ApiGatewayResponse.failed(((YunhuniApiException) ex).getCode(),ex.getMessage());
        }else{
            failed = ApiGatewayResponse.failed(ApiReturnCodeEnum.UnknownFail.getCode(), ApiReturnCodeEnum.UnknownFail.getMsg());
        }
        return JSONUtil2.objectToJson(failed);
    }
}
