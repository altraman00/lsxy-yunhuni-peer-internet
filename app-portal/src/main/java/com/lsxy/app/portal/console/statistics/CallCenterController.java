package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Created by zhangxb on 2016/10/24.
 */
@Controller
@RequestMapping("/console/statistics/callcenter")
public class CallCenterController extends AbstractPortalController {
    public static final String TYPE_MONTH = "month";//月统计类型 按年查找输出 返回按年
    public static final String TYPE_DAY = "day";//日统计类型 按月查找输出 返回按月
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        List<App> appList = (List)getBillAppList(request, App.PRODUCT_CALL_CENTER).getData();
        mav.addObject("appList",appList);
        Date date = new Date();
        String day = DateUtils.formatDate(date,"yyyy-MM");
        String month = DateUtils.formatDate(date,"yyyy");
        mav.addObject("day",day);
        mav.addObject("month",month);
        mav.setViewName("/console/statistics/callcenter/index");
        return mav;
    }
    @RequestMapping(value = "/get_current_month",method = RequestMethod.GET)
    @ResponseBody
    public RestResponse getCurrentMonth(HttpServletRequest request,String appId){
        String token = getSecurityToken(request);
        String url = PortalConstants.REST_PREFIX_URL + "/rest/day_statics/call_center/get?appId={1}";
       return RestRequest.buildSecurityRequest(token).get(url, Map.class,appId);
    }

    @RequestMapping("/detail")
    public ModelAndView detail(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize,
                               String appId, String startTime, String endTime, String type, String callnum, String agent){
        ModelAndView mav = new ModelAndView();
        Map<String,String> map = init(request,startTime,appId,App.PRODUCT_CALL_CENTER);
        if(StringUtils.isEmpty(startTime)){
            startTime = map.get("time");
        }
        if(StringUtils.isEmpty(endTime)){
            endTime = map.get("time");
        }
        if(StringUtils.isBlank(appId)){
            appId = map.get("appId");
        }
        map.put("type",type);
        map.put("callnum",callnum);
        map.put("agent",agent);
        map.put("startTime",startTime);
        map.put("endTime",endTime);
        mav.addAllObjects(map);
        String token = getSecurityToken(request);
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/call_center/plist?pageNo={1}&pageSize={2}&appId={3}&startTime={4}&endTime={5}&type={6}&callnum={7}&agent={8}";
        RestResponse<Page<CallCenter>> restRequest =  RestRequest.buildSecurityRequest(token).getPage(uri,CallCenter.class,pageNo,pageSize,appId,startTime,endTime,type,callnum,agent);
        String uri2 = PortalConstants.REST_PREFIX_URL  + "/rest/call_center/sum?appId={1}&startTime={2}&endTime={3}&type={4}&callnum={5}&agent={6}";
        RestResponse restResponse2 = RestRequest.buildSecurityRequest(token).get(uri2, Map.class,appId,startTime,endTime,type,callnum,agent);
        mav.addObject("sum",restResponse2.getData());
        mav.addObject("pageObj",restRequest.getData());
        mav.setViewName("/console/statistics/callcenter/detail");
        return mav;
    }
    /**
     * 下载
     * @param request
     * @param appId 应用id
     * @return
     */
    @RequestMapping("/download")
    public void download(HttpServletRequest request, HttpServletResponse response, String appId){
        String oType = CallSession.TYPE_VOICE_CALLBACK;
        String title = "呼叫中心";
        String one = "";
        String[] headers = new String[]{"呼叫时间","呼叫类型","主叫","被叫","坐席","转接结果","通话结束原因","转人工时间","接听时间","通话结束时间","消费金额"};
        String[] values = new String[]{"startTime","type:1=呼入;2=呼出","fromNum","toNum","agent","toManualResult:1=接听;2=呼叫坐席失败;3=主动放弃;4=超时","overReason","toManualTime","answerTime","endTime","cost"};
        String serviceType = App.PRODUCT_CALL_CENTER;
        List list = null;
        if(App.PRODUCT_CALL_CENTER.equals(serviceType)){
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String type = request.getParameter("type");
            String callnum = request.getParameter("callnum");
            String agent = request.getParameter("agent");
            String uri = PortalConstants.REST_PREFIX_URL  + "/rest/call_center/list?appId={1}&startTime={2}&endTime={3}&type={4}&callnum={5}&agent={6}";
            RestResponse restResponse = RestRequest.buildSecurityRequest(getSecurityToken(request)).getList(uri, CallCenter.class,appId,startTime,endTime,type,callnum,agent);
            list = (List)restResponse.getData();
            one =  title+" 时间："+startTime+" 至 "+endTime +" 坐席："+agent +" 呼叫号码："+callnum+" 类型：";
            if(CallCenter.CALL_UP==Integer.valueOf(type)){
                one +=" 呼出 ";
            }else if(CallCenter.CALL_IN==Integer.valueOf(type)){
                one += " 呼入 ";
            }else{
                one +="  ";
            }
        }
        String appName = "";
        if(StringUtils.isNotEmpty(appId)){
            appName = ((App)getAppById(request,appId).getData()).getName();
        }else{
            appName = "全部";
        }
        one += " 应用："+appName;
        downloadExcel(title,one,headers,values,list,null,"cost",response);
    }
    /**
     * 处理初始条件
     * @param request
     * @param time
     * @param appId
     * @return
     */
    public Map init(HttpServletRequest request, String time, String appId,String serviceType){
        Map map = new HashMap();
        List<App> appList = (List<App>)getBillAppList(request,serviceType).getData();
        map.put("appList",appList);
        if(StringUtil.isEmpty(appId)){
            if(StringUtils.isEmpty(appId)){
                if(appList.size()>0) {
                    appId = appList.get(0).getId();
                }
            }
        }
        map.put("appId",appId);
        if(StringUtils.isEmpty(time)){
            time = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        map.put("time",time);
        return map;
    }

}
