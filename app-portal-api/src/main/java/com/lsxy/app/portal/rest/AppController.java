package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.app.model.App;
import com.lsxy.yuhuni.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用RestApp
 * Created by liups on 2016/6/29.
 */
@RequestMapping("/rest/app")
@RestController
public class AppController extends AbstractRestController {
    @Autowired
    AppService appService;

    /**
     * 查找当前用户的应用
     * @throws Exception
     */
    @RequestMapping("/list")
    public RestResponse listApp() throws Exception{
        List<App> apps = appService.findAppByUserName(getCurrentAccountUserName());
        return RestResponse.success(apps);
    }
}
