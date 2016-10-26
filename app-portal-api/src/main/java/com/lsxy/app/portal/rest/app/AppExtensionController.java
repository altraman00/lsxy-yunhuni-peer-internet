package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.service.AppExtensionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangxb on 2016/10/21.
 */
@RequestMapping("/rest/app_extension")
@RestController
public class AppExtensionController extends AbstractRestController {
    private static final Logger logger = LoggerFactory.getLogger(AppExtensionController.class);
    @Autowired
    AppExtensionService appExtensionService;
    @RequestMapping("/list/{appId}")
    public RestResponse getOnlineAction(@PathVariable String appId){
        List list = appExtensionService.findByAppId(appId);
        return RestResponse.success(list);
    }
}
