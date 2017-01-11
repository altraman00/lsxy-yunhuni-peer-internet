package com.lsxy.app.portal.console.telenum;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 呼入号码管理
 * Created by zhangxb on 2016/6/30.
 */
@Controller
@RequestMapping("/console/telenum/callnum")
public class RentTelnumController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(RentTelnumController.class);

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
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/res_rent/list?pageNo={1}&pageSize={2}";
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
    public RestResponse release(HttpServletRequest request,String id){
        RestResponse restResponse = releaseREST(request,id);
        return restResponse;
    }
    /**
     * 根据id释放手机号码
     * @param request
     * @param id 租户号码id
     * @return
     */
    private RestResponse releaseREST(HttpServletRequest request,String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/res_rent/release?id={1}";
        return  RestRequest.buildSecurityRequest(token).get(uri, String.class,id);
    }
    /** 获取号码列表
     * **/
    @RequestMapping("/telnum/plist" )
    @ResponseBody
    public RestResponse telnumPlist(HttpServletRequest request, @RequestParam(defaultValue = "1")Integer pageNo,  @RequestParam(defaultValue = "20")Integer pageSize,
                              String telnum,String type,String areaCode,String order
    ){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL + "/rest/res_rent/telnum/plist?pageNo={1}&pageSize={2}&telnum={3}&type={4}&areaCode={5}&order={6}";
        return  RestRequest.buildSecurityRequest(token).getPage(uri, ResourceTelenum.class,pageNo,pageSize,telnum,type,areaCode,order);
    }
    /** 获取用户的号码未支付订单
     * **/
    @RequestMapping("/telnum/order" )
    @ResponseBody
    public RestResponse telnumOrder(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/res_rent/telnum/order";
        return  RestRequest.buildSecurityRequest(token).get(uri, Map.class);
    }
    /** 支付订单
     * **/
    @RequestMapping("/telnum/order/play/{id}" )
    @ResponseBody
    public RestResponse telnumPlay(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/res_rent/telnum/order/play/{id}";
        return  RestRequest.buildSecurityRequest(token).get(uri,String.class,id);
    }
    /** 取消订单
     * **/
    @RequestMapping("/telnum/order/delete/{id}" )
    @ResponseBody
    public RestResponse telnumDelete(HttpServletRequest request, @PathVariable String id){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/res_rent/telnum/order/delete/{id}";
        return  RestRequest.buildSecurityRequest(token).get(uri,String.class,id);
    }
    /** 创建订单
     * **/
    @RequestMapping("/telnum/order/new" )
    @ResponseBody
    public RestResponse telnumNew(HttpServletRequest request, String ids){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/res_rent/telnum/order/new?ids={1}";
        return  RestRequest.buildSecurityRequest(token).get(uri,TelenumOrder.class,ids);
    }
    @RequestMapping("/province/list" )
    @ResponseBody
    public RestResponse getProvinceList(HttpServletRequest request){
        List list = getTelnumLocationProvinceList(request);
        return  RestResponse.success(list);
    }
    @RequestMapping("/city/list" )
    @ResponseBody
    public RestResponse getCityList(HttpServletRequest request){
        List list = getTelnumCity(request);
        return  RestResponse.success(list);
    }

    @RequestMapping("/list/app/{appId}")
    @ResponseBody
    public Page<ResourcesRent> findByAppId(HttpServletRequest request,@PathVariable String appId,Integer pageNo,Integer pageSize){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL +   "/rest/res_rent/list/app/{1}?pageNo={2}&pageSize={3}";
        return RestRequest.buildSecurityRequest(token).getPage(uri, ResourcesRent.class,appId,pageNo,pageSize).getData();
    }

}
