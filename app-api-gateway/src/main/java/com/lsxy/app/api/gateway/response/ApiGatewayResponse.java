package com.lsxy.app.api.gateway.response;

import com.lsxy.framework.core.exceptions.api.ApiReturnCodeEnum;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by liups on 2016/9/2.
 */
public class ApiGatewayResponse<T> {

    private static final Logger logger = LoggerFactory.getLogger(RestResponse.class);
    private String code;    //返回码

    private String msg;     //返回信息

    private T data;         //数据

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     * 提供成功的静态方法，方便使用
     * @return
     */
    public static ApiGatewayResponse success(){
        return success(null);
    }
    /**
     * 提供成功的静态方法，方便使用
     * @param data 希望作为返回值输出的结果对象
     * @return
     */
    public static ApiGatewayResponse success(Object data){
        ApiGatewayResponse response = new ApiGatewayResponse();
        response.setCode(ApiReturnCodeEnum.Success.getCode());
        response.setData(data);
        response.setMsg(ApiReturnCodeEnum.Success.getMsg());
        if(logger.isDebugEnabled()){
            logger.debug("输出响应："+response);
        }
        return response;
    }

    /**
     * 提供失败对象的静态方法，方便输出
     * @param code  错误码
     * @param msg   错误消息
     * @return
     */
    public static ApiGatewayResponse failed(String code,String msg){
        ApiGatewayResponse response = new ApiGatewayResponse();
        response.setCode(code);
        response.setMsg(msg);
        return response;
    }

    /**
     * 包装一个指定对象数据类型的响应对象
     * @param <T>
     * @return
     */
    public <T> ApiGatewayResponse<T> wrapIndicateTypeApiGatewayResponse(T data) {
        ApiGatewayResponse<T> response = new ApiGatewayResponse<T>();
        response.setCode(this.getCode());
        response.setMsg(this.getMsg());
        response.setData(data);
        return response;
    }

    @Override
    public String toString() {
        return JSONUtil.objectToJson(this);
    }
}
