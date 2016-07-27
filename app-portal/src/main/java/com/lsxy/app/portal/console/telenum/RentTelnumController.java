package com.lsxy.app.portal.console.telenum;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.MapBean;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/6/30.
 */
@Controller
@RequestMapping("/console/telenum/callnum")
public class RentTelnumController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(RentTelnumController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");

    /**
     * 呼入号码管理首页
     * @param request
     * @param pageNo 请求的页面
     * @return
     */
    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1")Integer pageNo,  @RequestParam(defaultValue = "20")Integer pageSize){
        ModelAndView mav = new ModelAndView();
        RestResponse<Page<ResourcesRent>> restResponse = pageList(request,pageNo,pageSize);
        Page<ResourcesRent> pageObj= restResponse.getData();
        mav.addObject("pageObj",pageObj);
        mav.addObject("time",new Date());
        mav.setViewName("/console/telenum/callnum/index");
        return mav;
    }

    /**
     * 查询租户下所有号码的rest请求
     * @param request
     * @param pageNo 请求的页面
     * @param pageSize 每页多少条数据
     * @return
     */
    private RestResponse pageList(HttpServletRequest request,Integer  pageNo,Integer pageSize){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/res_rent/list?pageNo={1}&pageSize={2}";
        return  RestRequest.buildSecurityRequest(token).getPage(uri, ResourcesRent.class,pageNo,pageSize);
    }

    /**
     * 根据id释放手机号码
     * @param request
     * @param id 租户号码id
     * @return
     */
    @RequestMapping("/release" )
    @ResponseBody
    public MapBean release(HttpServletRequest request,String id){
        MapBean map = new MapBean();
        RestResponse restResponse = releaseREST(request,id);
        if(restResponse.isSuccess()){
            map.setCode("0000");
            map.setMsg("释放号码成功");
        }else{
            map.setCode(restResponse.getErrorCode());
            map.setMsg(restResponse.getErrorMsg());
        }
        return map;
    }
    /**
     * 根据id释放手机号码
     * @param request
     * @param id 租户号码id
     * @return
     */
    private RestResponse releaseREST(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/res_rent/release?id={1}";
        return  RestRequest.buildSecurityRequest(token).get(uri, ResourcesRent.class,id);
    }
}
