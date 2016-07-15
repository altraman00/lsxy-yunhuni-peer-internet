package com.lsxy.app.portal.rest.app;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * 查找当前用户的应用
     * @throws Exception
     */
    @RequestMapping("/list")
    public RestResponse listApp() throws Exception{
        List<App> apps = appService.findAppByUserName(getCurrentAccountUserName());
        return RestResponse.success(apps);
    }

    /**
     * 查找用户下的分页信息
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    @RequestMapping("/page_list")
    public RestResponse pageList(Integer pageNo,Integer pageSize){
        String userName = getCurrentAccountUserName();
        Page<App> page = appService.pageList(userName,pageNo,pageSize);
        return RestResponse.success(page);
    }

    /**
     * 删除应用
     * @param id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse save(String id) throws InvocationTargetException, IllegalAccessException {
        App resultApp = appService.findById(id);
        appService.delete(resultApp);
        return RestResponse.success(resultApp);
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


}
