package com.lsxy.app.portal.rest.app;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.call.center.api.service.AppExtensionService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@RequestMapping("/rest/app_extension")
@RestController
public class AppExtensionController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppExtensionController.class);

    @Reference(timeout = 3000,check = false,lazy = true)
    AppExtensionService appExtensionService;

    @RequestMapping("/list/{appId}")
    public RestResponse getOnlineAction(@PathVariable String appId, Integer pageNo,Integer pageSize){
        Page page = appExtensionService.getPage(appId,pageNo,pageSize);
        return RestResponse.success(page);
    }
}
