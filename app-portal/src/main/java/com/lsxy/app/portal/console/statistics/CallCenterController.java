package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.statistics.model.ApiCallDay;
import com.lsxy.yunhuni.api.statistics.model.ApiCallMonth;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by zhangxb on 2016/10/24.
 */
@Controller
@RequestMapping("/console/statistics/callcenter")
public class CallCenterController extends AbstractPortalController {
    public static final String TYPE_MONTH = "month";//月统计类型 按年查找输出 返回按年
    public static final String TYPE_DAY = "day";//日统计类型 按月查找输出 返回按月
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,String appId){
        ModelAndView mav = new ModelAndView();
        if(StringUtils.isNotEmpty(appId)){

        }
        mav.addObject("appId",appId);
        List<App> appList = (List)getBillAppList(request, App.PRODUCT_CALL_CENTER).getData();
        mav.addObject("appList",appList);
        String time = DateUtils.formatDate(new Date(),"yyyy-MM");
        mav.addObject("time",time);
        mav.setViewName("/console/statistics/callcenter/index");
        return mav;
    }
    @RequestMapping("/get/statistics")
    @ResponseBody
    public RestResponse getStatistics(HttpServletRequest request, String appId,String time){
        try{
            DateUtils.parseDate(time,"yyyy-MM");
        }catch (Exception e){
            return RestResponse.failed("0000","日期格式错误");
        }
        return RestResponse.success();
    }
    @RequestMapping("/get/list")
    @ResponseBody
    public RestResponse getList(HttpServletRequest request, String type, String appId, String stratTime){
        try{
            DateUtils.parseDate(stratTime,"yyyy-MM");
        }catch (Exception e){
            return RestResponse.failed("0000","日期格式错误");
        }
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/api_call_"+type+"/list?tenantId={1}&appId={2}&startTime={3}";
//        Class clazz = ApiCallDay.class;
//        if(ConsumeStatisticsVo.TYPE_MONTH.equals(type)){
//            clazz = ApiCallMonth.class;
//        }
//        appId = "-1".equals(appId)?null:appId;
//        String tenantId = getCurrentAccount(request).getTenant().getId();
//         (List) RestRequest.buildSecurityRequest(token).getList(uri, clazz,tenantId,appId,time).getData();
        return RestResponse.success();
    }


}
