package com.lsxy.app.oc.rest.app;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用RestApp
 * Created by zhangxb on 2016/8/10.
 */
@RequestMapping("/app")
@RestController
public class AppController extends AbstractRestController {
    @Autowired
    private AppService appService;
    @Autowired
    AppOnlineActionService appOnlineActionService;
    /**
     * 查找当前用户的应用
     * @throws Exception
     */
    @RequestMapping(value = "/list/{uid}",method = RequestMethod.GET)
    public RestResponse listApp(@PathVariable String uid) throws Exception{
        List<App> apps = appService.findAppByUserName(uid);
        return RestResponse.success(apps);
    }

    /**
     * 查找用户下的分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/plist/{uid}")
    public RestResponse pageList(@PathVariable String uid,@RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20" )Integer pageSize){
        Page<App> page = appService.pageList(uid,pageNo,pageSize);
        return RestResponse.success(page);
    }

}
