package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 应用RestApp
 * Created by liups on 2016/6/29.
 */
@RequestMapping("/rest/app")
@RestController
public class AppController extends AbstractRestController {
    @Autowired
    private AppService appService;
    @Autowired
    private TenantService tenantService;
    @Autowired
    AppOnlineActionService appOnlineActionService;

    /**
     * 根据应用名字查找应用数
     * @param name 应用名字
     * @return
     */
    @RequestMapping("/count/{name}")
    public RestResponse countByTenantIdAndName(@PathVariable String name){
        long re = appService.countByTenantIdAndName(getCurrentAccount().getTenant().getId(),name);
        return RestResponse.success(re);
    }
    /**
     * 查找当前用户的应用
     * @throws Exception
     */
    @RequestMapping("/list")
    public RestResponse listApp() throws Exception{
        List<App> apps = appService.findAppByUserName(getCurrentAccount().getTenant().getId());
        return RestResponse.success(apps);
    }

    /**
     * 查找用户下的分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/plist")
    public RestResponse pageList(Integer pageNo,Integer pageSize){
        Page<App> page = appService.pageList(getCurrentAccount().getTenant().getId(),pageNo,pageSize);
        return RestResponse.success(page);
    }

    /**
     * 根据appId查找应用
     * @param id 应用id
     * @return
     */
    @RequestMapping("/get/{id}")
    public RestResponse findById(@PathVariable String id){
        App app = appService.findById(id);
        return RestResponse.success(app);
    }
    /**
     * 删除应用
     * @param id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        boolean flag = appService.isAppBelongToUser(getCurrentAccountUserName(), id);
        if(flag){
            App resultApp = appService.findById(id);
            appService.delete(resultApp);
            return RestResponse.success();
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }
    /**
     * 新建应用
     * @param app app对象
     * @return
     */
    @RequestMapping("/create")
    public RestResponse create(App app ){
        String userName = getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        app.setTenant(tenant);
        app = appService.save(app);
        return RestResponse.success(app);
    }
    /**
     * 新建应用
     * @param app app对象
     * @return
     */
    @RequestMapping("/update")
    public RestResponse update(App app ) throws InvocationTargetException, IllegalAccessException {
        String userName = getCurrentAccountUserName();
        boolean flag = appService.isAppBelongToUser(userName, app.getId());
        if(flag){
            App resultApp = appService.findById(app.getId());
            EntityUtils.copyProperties(resultApp,app);
            resultApp = appService.save(resultApp);
            //应用修改后，将其上线步骤清空
            appOnlineActionService.resetAppOnlineAction(app.getId());
            return RestResponse.success(resultApp);
        }else{
            return RestResponse.failed("0000","应用不属于用户");
        }
    }
}
