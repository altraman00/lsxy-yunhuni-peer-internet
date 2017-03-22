package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangxb on 2017/2/21.
 */
@Controller
@RequestMapping("/console/statistics/subaccount")
public class SubAccountStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(SubAccountStatisticsController.class);
    /**
     * 消费统计首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String monthTime, String dayTime, String appId,String stime,String subId){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        List<App> appList = (List<App>) getAppList(request).getData();
        if(StringUtils.isEmpty(appId)){
            appId = appList.get(0).getId();
            mav.addObject("appServiceTyp",appList.get(0).getServiceType());
        }else{
            App app = (App)getAppById(request,appId).getData();
            mav.addObject("appServiceTyp",app.getServiceType());
        }
        if(StringUtils.isEmpty(monthTime)){
            monthTime = DateUtils.formatDate(new Date(),"yyyy-MM");
        }
        if(StringUtils.isEmpty(dayTime)){
            dayTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        if(StringUtils.isEmpty(stime)){
            stime = "day";
        }
        String startTime ;
        if("day".equals(stime)){
            startTime = dayTime;
        }else{
            startTime = monthTime;
        }
        mav.addObject("monthTime",monthTime);
        mav.addObject("appId",appId);
        mav.addObject("stime",stime);
        mav.addObject("dayTime",dayTime);
        //参数初始化完成
        //获取参数记录
        String token = getSecurityToken(request);
        Account account = getCurrentAccount(request);
        //tenantId, String appId, String subId, String startTime, String endTime
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/plist?pageNo={1}&pageSize={2}&tenantId={3}&appId={4}&subId={5}&startTime={6}&endTime={7}";
        RestResponse result = RestRequest.buildSecurityRequest(token).getPage(uri, SubaccountStatisticalVO.class, pageNo, pageSize, account.getTenant().getId(), appId,subId,startTime,null);
        String uri2 =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/sum?tenantId={1}&appId={2}&subId={3}&startTime={4}&endTime={5}";
        RestResponse result2 = RestRequest.buildSecurityRequest(token).get(uri2, Map.class, account.getTenant().getId(), appId,subId,startTime,null);
        mav.addObject("sum",result2.getData());
        mav.addObject("pageObj",result.getData());
        mav.setViewName("/console/statistics/subaccount/index");
        return mav;
    }
    @RequestMapping("/download")
    @ResponseBody
    public String download(HttpServletRequest request, HttpServletResponse response, String monthTime, String dayTime, String appId,String stime,String subId) {
        String appId1 = appId;
        //初始化数据
        if(StringUtil.isEmpty(appId)){
            appId = "all";
        }
        if("all".equals(appId)){
            appId1 = "";
        }
        if(StringUtils.isEmpty(monthTime)){
            monthTime = DateUtils.formatDate(new Date(),"yyyy-MM");
        }
        if(StringUtils.isEmpty(dayTime)){
            dayTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        if(StringUtils.isEmpty(stime)){
            stime = "day";
        }
        String startTime ;
        String title = "子账号综合统计";
        String one = "";
        String[] headers = null;
        String[] values = null;
        if("day".equals(stime)){
            startTime = dayTime;
            one +=" 类型：日统计 时间："+startTime;

        }else{
            startTime = monthTime;
            one +=" 类型：月统计 时间："+startTime;
        }
        if("all".equals(appId)){
            one += " 选中应用：全部应用 ";
            headers = new String[]{"鉴权账号", "密钥", "所属应用", "话务量（分钟）", "消费金额（元）","语音总用量 /配额（分钟）","坐席数/配额（个）"};
            values = new String[]{"certId", "secretKey", "appName", "amongDuration", "amongAmount","voiceNum","seatNum"};
        }else{
            App app = getAppById(request,appId).getData();
            one += " 选中应用："+app.getName();
            headers = new String[]{"鉴权账号", "密钥", "话务量（分钟）", "消费金额（元）","语音总用量 /配额（分钟）","坐席数/配额（个）"};
            values = new String[]{"certId", "secretKey", "amongDuration", "cost", "amongAmount","voiceNum","seatNum"};
        }

        //参数初始化完成
        //获取参数记录
        String token = getSecurityToken(request);
        Account account = getCurrentAccount(request);
        //tenantId, String appId, String subId, String startTime, String endTime
        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/list?tenantId={1}&appId={2}&subId={3}&startTime={4}&endTime={5}";
        RestResponse<List<SubaccountStatisticalVO>> result = RestRequest.buildSecurityRequest(token).getList(uri, SubaccountStatisticalVO.class,  account.getTenant().getId(), appId1,subId,startTime,null);
        String uri2 =  PortalConstants.REST_PREFIX_URL  + "/rest/api_sub_account_"+stime+"/sum?tenantId={1}&appId={2}&subId={3}&startTime={4}&endTime={5}";
        RestResponse<Map> result2 = RestRequest.buildSecurityRequest(token).get(uri2, Map.class, account.getTenant().getId(), appId1,subId,startTime,null);
        //amongAmount
        //amongDuration
        String amongAmount = result2.getData().get("amongAmount")==null? "": result2.getData().get("amongAmount").toString();
        if(StringUtils.isNotEmpty(amongAmount)){
            one += " 总消费："+amongAmount+"元";
        }else{
            one += " 总消费：0元";
        }
        List list = result.getData();

        downloadExcel(title, one, headers, values, list, null, "amongAmount", response);
        return "";
    }
}
