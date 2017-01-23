package com.lsxy.app.portal.rest.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@RequestMapping("/rest/callcenter")
@RestController
public class AppCallcenterController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppCallcenterController.class);

    @Reference(timeout = 3000,check = false,lazy = true)
    AppExtensionService appExtensionService;
    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;

    @RequestMapping("/{appId}/app_extension/page")
    public RestResponse listExtensions(HttpServletRequest request, @PathVariable String appId,
                                       @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                       @RequestParam(defaultValue = "20",required = false)  Integer pageSize) throws YunhuniApiException {
        Page page = appExtensionService.getPage(appId,pageNo,pageSize);
        return RestResponse.success(page);
    }

    @RequestMapping(value = "/{appId}/agent/page",method = RequestMethod.GET)
    public RestResponse page(HttpServletRequest request, @PathVariable String appId,
                             @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                             @RequestParam(defaultValue = "20",required = false)  Integer pageSize) throws YunhuniApiException {
        Page page  = callCenterAgentService.getPage(appId,pageNo,pageSize);
        return RestResponse.success(page);
    }

}
