package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.callcenter.vo.ExtensionVO;
import com.lsxy.call.center.api.model.AppExtension;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liups on 2016/11/14.
 */
@RestController
public class ExtensionController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(ExtensionController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private AppExtensionService appExtensionService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/callcenter/extension",method = RequestMethod.POST)
    public ApiGatewayResponse createExtension(HttpServletRequest request, @RequestBody AppExtension extension, @RequestHeader("AppID") String appId) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        AppExtension rExtension = appExtensionService.create(appId,extension);
        ExtensionVO vo = new ExtensionVO();
        try {
            BeanUtils.copyProperties(vo,rExtension);
        } catch (Exception e) {
        }
        return ApiGatewayResponse.success(vo);
    }

    @RequestMapping(value = "/{account_id}/callcenter/extension/{extension_id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse createExtension(HttpServletRequest request, @PathVariable String extension_id,@RequestHeader("AppID") String appId) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        appExtensionService.delete(extension_id,appId);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/extension/{extension_id}",method = RequestMethod.GET)
    public ApiGatewayResponse listExtensions(HttpServletRequest request,@RequestHeader("AppID") String appId,@PathVariable("extension_id") String id) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        AppExtension extension = appExtensionService.findOne(appId, id);
        ExtensionVO vo = new ExtensionVO();
        try {
            BeanUtils.copyProperties(vo,extension);
        } catch (Exception e) {
        }
        return ApiGatewayResponse.success(vo);
    }

    @RequestMapping(value = "/{account_id}/callcenter/extension",method = RequestMethod.GET)
    public ApiGatewayResponse listExtensions(HttpServletRequest request,@RequestHeader("AppID") String appId,
                                             @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                             @RequestParam(defaultValue = "20",required = false)  Integer pageSize) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Page page = appExtensionService.getPage(appId,pageNo,pageSize);
        List<AppExtension> result = page.getResult();
        List<ExtensionVO> returnResult = new ArrayList<>();
        if(result != null){
            result.stream().forEach(extension -> {
                ExtensionVO vo = new ExtensionVO();
                try {
                    BeanUtils.copyProperties(vo,extension);
                    returnResult.add(vo);
                } catch (Exception e) {
                }
            });
        }
        page.setResult(returnResult);
        return ApiGatewayResponse.success(page);
    }

    @RequestMapping(value = "/{account_id}/callcenter/extensionlisttest",method = RequestMethod.GET)
    public ApiGatewayResponse extensionlisttest(HttpServletRequest request,@RequestHeader("AppID") String appId) throws YunhuniApiException {
        return ApiGatewayResponse.success(appExtensionService.exs(appId));
    }
}
