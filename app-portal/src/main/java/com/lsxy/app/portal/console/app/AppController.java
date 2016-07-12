package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 应用
 * Created by zhangxb on 2016/7/11.
 */
@Controller
@RequestMapping("/console/app/")
public class AppController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(AppController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 应用首页
     * @param request
     * @return
     */
    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        ModelAndView mav = new ModelAndView();
        RestResponse<Page<App>> restResponse = pageList(request,pageNo,pageSize);
        Page<App> pageObj = restResponse.getData();
        mav.addObject("msg",request.getSession().getAttribute("msg"));
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/app/list");
        return mav;
    }
    /**
     * 创建应用首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/app/index");
        return mav;
    }
    /**
     * 获取分页信息
     * @param request
     * @param pageNo
     * @param pageSize
     * @return
     */
    private RestResponse pageList(HttpServletRequest request,Integer pageNo,Integer pageSize){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/app/page_list?pageNo={1}&pageSize={2}";
        return RestRequest.buildSecurityRequest(token).getPage(uri,App.class,pageNo,pageSize);
    }
    /**
     * 操作应用
     * @param request
     * @return
     */
    @RequestMapping("/save")
    @ResponseBody
    public Map save(HttpServletRequest request, App app,String operate){
        String msg = "无效操作";
        if("delete".equals(operate)){//删除应用
            msg = "删除成功";
        }else if("create".equals(operate)){//新增应用
            app.setStatus(App.STATUS_NOT_ONLINE);//设置状态为未上线
            msg = "新建应用成功";
        }
        App resultApp = (App)saveApp(request,app,operate).getData();
        if(resultApp==null){
            msg = "无效操作";
        }
        Map map = new HashMap();
        map.put("msg",msg);
        return map;
    }

    /**
     * 操作应用
     * @param request
     * @param app 应用对象
     * @param operate 操作类型 删除delete，更新update，新建create
     * @return
     */
    private RestResponse saveApp(HttpServletRequest request,App app,String operate){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/save";
        Map<String, Object> map = EntityUtils.toMap(app);
        map.put("operate",operate);
        return RestRequest.buildSecurityRequest(token).post(uri,map, App.class);
    }
}
