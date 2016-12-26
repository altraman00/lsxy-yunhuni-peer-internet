package com.lsxy.app.oc.rest.app;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 应用RestApp
 * Created by zhangxb on 2016/8/10.
 */
@Api(value = "应用接口" )
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
    @ApiOperation(value = "查找当前用户的应用")
    public RestResponse listApp(
            @ApiParam(name = "uid",value = "用户id") @PathVariable String uid,
            @ApiParam(name = "serviceType",value = "应用类型 语言产品voice呼叫中心产品call_center") @RequestParam(required = false) String serviceType
    ) throws Exception{
        List<App> apps = null;
        if(StringUtils.isNotEmpty(serviceType)){
            apps = appService.findAppByTenantIdAndServiceType(uid,serviceType);
        }else{
            apps = appService.findAppByUserName(uid);
        }
        return RestResponse.success(apps);
    }

    /**
     * 查找用户下的分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping(value ="/plist/{uid}",method = RequestMethod.GET)
    @ApiOperation(value = "查找用户下的分页信息")
    public RestResponse pageList(
            @ApiParam(name = "uid",value = "用户id")
            @PathVariable String uid,
            @RequestParam(defaultValue = "1") Integer pageNo,
            @RequestParam(defaultValue = "20" )Integer pageSize
    ){
        Page<App> page = appService.pageList(uid,pageNo,pageSize);
        return RestResponse.success(page);
    }

}
