package com.lsxy.app.portal.console.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.console.telenum.TestNumBindController;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.resourceTelenum.model.TestNumBind;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
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
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/app/list");
        return mav;
    }
    /**
     * 获取租户下所有测试绑定号码
     * @param request
     * @return
     */
    private RestResponse getTestNumBindList(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/test_num_bind/list";
        return  RestRequest.buildSecurityRequest(token).getList(uri, TestNumBind.class);
    }
    /**
     * 创建应用首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,String id){
        ModelAndView mav = new ModelAndView();
        if(id!=null&&id.length()>0) {//修改时使用
            RestResponse<App> restResponse = findById(request, id);
            App app = restResponse.getData();
            mav.addObject("app", app);
        }
        mav.setViewName("/console/app/index");
        return mav;
    }

    /**
     * 详情页入口
     * @param request
     * @param id
     * @return
     */
    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request,String id){
        ModelAndView mav = new ModelAndView();
        RestResponse<App> restResponse = findById(request, id);
        App app = restResponse.getData();
        mav.addObject("app", app);
        List<TestNumBind> testNumBindList = (List<TestNumBind>)getTestNumBindList(request).getData();
        mav.addObject("testNumBindList",testNumBindList);
        mav.setViewName("/console/app/detail");
        return mav;
    }
    /**
     * 根据id查找应用
     * @param request
     * @param id
     * @return
     */
    private RestResponse findById(HttpServletRequest request,String id ){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/find_by_id?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, App.class,id);
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
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(HttpServletRequest request,String id){
        deleteApp(request,id);
        Map map = new HashMap();
        map.put("msg","删除成功");
        return map;
    }
    /**
     * 新建应用
     * @param request
     * @return
     */
    @RequestMapping("/create")
    @ResponseBody
    public Map create(HttpServletRequest request, App app){
        app.setStatus(App.STATUS_NOT_ONLINE);//设置状态为未上线
        createApp(request,app);
        Map map = new HashMap();
        map.put("msg","新建应用成功");
        return map;
    }
    /**
     * 删除应用
     * @param request
     * @param id 应用id
     * @return
     */
    private RestResponse deleteApp(HttpServletRequest request,String id ){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri, App.class,id);
    }
    /**
     * 更新应用
     * @param request
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public Map update(HttpServletRequest request, App app){
        updateApp(request,app);
        Map map = new HashMap();
        map.put("msg","应用修改成功");
        return map;
    }
    /**
     * 新建应用
     * @param request
     * @param app 应用对象
     * @return
     */
    private RestResponse createApp(HttpServletRequest request,App app){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/create";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(app, Map.class);
        return RestRequest.buildSecurityRequest(token).post(uri,map, App.class);
    }
    /**
     * 更新应用
     * @param request
     * @param app 应用对象
     * @return
     */
    private RestResponse updateApp(HttpServletRequest request,App app){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/update";
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.convertValue(app, Map.class);
        return RestRequest.buildSecurityRequest(token).post(uri,map, App.class);
    }
}
