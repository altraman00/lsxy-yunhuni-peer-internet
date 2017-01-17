package com.lsxy.app.api.gateway.rest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.framework.core.exceptions.api.ApiReturnCodeEnum;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Tandy on 2016/6/28.
 */
@RequestMapping("/${api.gateway.version}/account/")
public class AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAPIController.class);

    /**
     * 对Controller层统一的异常处理
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ApiGatewayResponse exp(HttpServletRequest request, HttpServletResponse response, Exception ex) {
        ApiGatewayResponse failed;
        Throwable resultEx;
        Throwable cause = ex.getCause();
        if(cause != null){
            resultEx = cause;
        }else{
            resultEx = ex;
        }
        if(resultEx instanceof YunhuniApiException){
            failed = ApiGatewayResponse.failed(((YunhuniApiException) resultEx).getCode(),resultEx.getMessage());
        }else if(resultEx instanceof MethodArgumentNotValidException || resultEx instanceof JsonMappingException){
            failed = ApiGatewayResponse.failed(ApiReturnCodeEnum.IllegalArgument.getCode(), ApiReturnCodeEnum.IllegalArgument.getMsg());
        }else{
            failed = ApiGatewayResponse.failed(ApiReturnCodeEnum.UnknownFail.getCode(), ApiReturnCodeEnum.UnknownFail.getMsg());
        }
        logger.error("调用接口出现异常：",resultEx);
        return failed;
    }
}
