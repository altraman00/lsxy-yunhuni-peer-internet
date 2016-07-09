package com.lsxy.app.api.gateway.rest;

import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/28.
 * 呼叫API入口
 */
@RestController
public class CallController extends AbstractAPIController{

    /**
     * 自动化测试用例使用
     * @param accountId
     * @return
     */
    @RequestMapping("/{accountId}/calltest")
    public RestResponse test(@PathVariable String accountId){
        return RestResponse.success(accountId);
    }

    @RequestMapping("/{accountId}/call")
    public RestResponse doCall(@PathVariable String accountId){
        return RestResponse.success(accountId);
    }

    @RequestMapping("/{accountId}/call/{callId}")
    public RestResponse getCall(@PathVariable String accountId,@PathVariable String callId){
        return RestResponse.success(callId);
    }


}
