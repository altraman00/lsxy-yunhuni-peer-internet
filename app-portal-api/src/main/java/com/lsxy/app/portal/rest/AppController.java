package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.base.IdEntity;
import com.lsxy.framework.core.utils.EntityUtils;
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
     * 更新应用信息
     * @param app app对象
     * @param operate 操作类型
     * @return
     */
    public RestResponse save(App app,String operate)throws InvocationTargetException, IllegalAccessException{
        App resultApp = null;
        if("delete".equals(operate)){//将应用更新为删除状态
            resultApp = appService.findById(app.getId());
            appService.delete(resultApp);
        }else if("update".equals(operate)){//更新应用信息
            resultApp = appService.findById(app.getId());
            EntityUtils.copyProperties(resultApp, app);
            resultApp = appService.save(resultApp);
        }else if("create".equals(operate)){//新建应用
            resultApp = appService.save(app);
        }
        return RestResponse.success(resultApp);
    }
}
