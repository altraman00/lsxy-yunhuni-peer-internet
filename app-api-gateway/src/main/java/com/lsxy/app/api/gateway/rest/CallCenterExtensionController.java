package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/11/14.
 */
@RestController
public class CallCenterExtensionController extends AbstractAPIController{
    private static final Logger logger = LoggerFactory.getLogger(CallCenterExtensionController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/callcenter/extension",method = RequestMethod.POST)
    public ApiGatewayResponse createExtension(HttpServletRequest request, @RequestBody AppExtension extension, @RequestHeader("AppID") String appId){
        App app = appService.findById(appId);
        extension.setAppId(appId);
        extension.setTenantId(app.getTenant().getId());
        appExtensionService.register(extension);
        //TODO 初始化状态状态
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/extension/{extension_id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse createExtension(HttpServletRequest request, @PathVariable String extension_id,@RequestHeader("AppID") String appId){
        AppExtension extension = appExtensionService.findById(extension_id);
        if(appId.equals(extension.getAppId())){

        }
        return null;
    }



}
