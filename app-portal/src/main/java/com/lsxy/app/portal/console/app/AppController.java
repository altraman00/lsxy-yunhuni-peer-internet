package com.lsxy.app.portal.console.app;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.config.SystemConfig;
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
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        ModelAndView mav = new ModelAndView();
        RestResponse<Page<App>> restResponse = pageList(request,pageNo,pageSize);
        Page<App> pageObj = restResponse.getData();
        mav.addObject("msg",request.getSession().getAttribute("msg"));
        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/app/list");
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
     * 删除应用
     * @param request
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map delete(HttpServletRequest request, String id){
        deleteApp(request,id);
        Map map = new HashMap();
        map.put("msg","删除成功");
        return map;
    }

    /**
     * 将应用状态改为删除
     * @param request
     * @param id 应用id
     * @return
     */
    private RestResponse deleteApp(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl + "/rest/app/delete?id={1}";
        return RestRequest.buildSecurityRequest(token).get(uri,App.class,id);
    }
}
