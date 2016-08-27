package com.lsxy.app.api.gateway.rest;

import com.lsxy.area.api.exceptions.YunhuniApiException;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

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
    public String exp(HttpServletRequest request, Exception ex) {
        ex.printStackTrace();
        RestResponse failed;
        if(ex instanceof YunhuniApiException){
            failed = RestResponse.failed(((YunhuniApiException) ex).getCode(),ex.getMessage());
        }else{
            failed = RestResponse.failed("111111", "未知错误");
        }
        return JSONUtil2.objectToJson(failed);
    }
}
