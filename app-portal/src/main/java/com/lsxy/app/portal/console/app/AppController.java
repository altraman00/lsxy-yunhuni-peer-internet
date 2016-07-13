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
        String uri = restPrefixUrl +   "/rest/app/delete?{1}";
        return RestRequest.buildSecurityRequest(token).get(uri, App.class,id);
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
}
