package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.yunhuni.api.statistics.model.*;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 会话统计
 * Created by zhangxb on 2016/7/8.
 */
@Controller
@RequestMapping("/console/statistics/session")
public class SessionStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(SessionStatisticsController.class);
    /**
     * 消费统计首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        Date date = new Date();
        mav.addObject("startTimeMonth", DateUtils.formatDate(date,"yyyy-MM"));
        mav.addObject("startTimeYear", DateUtils.formatDate(date,"yyyy"));
        mav.setViewName("/console/statistics/session/index");
        return mav;
    }

    /**
     * 异步获取图表显示信息
     * @param request
     * @param type 类型 日统计，月统计
     * @param startTime 统计时间
     * @param appId 应用
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RestResponse list(HttpServletRequest request,String type,String startTime,String appId,String callType){
        List list = new ArrayList();
        List tempConsumeList = getConsumeList(request,type,appId,startTime);
        List tempVoiceCdrList = getVoiceCdrList(request,type,appId,startTime,callType);
        Object date = 12;
        if(ConsumeStatisticsVo.TYPE_DAY.equals(type)){
            date = DateUtils.parseDate(startTime,"yyyy-MM");
        }
        list.add(getArrays(tempConsumeList,date,""));
        list.add(getArrays(tempVoiceCdrList,date,"amongCostTime"));
        list.add(getArrays(tempVoiceCdrList,date,"amongCall"));
        return RestResponse.success(list);
    }
    @RequestMapping("/list/call/time")
    @ResponseBody
    public RestResponse listCallTime(HttpServletRequest request,String type,String startTime,String appId){
        List list = new ArrayList();
        List tempVoiceCdrList = getVoiceCdrList(request,type,appId,startTime,App.PRODUCT_CALL_CENTER);
        Object date = 12;
        if(ConsumeStatisticsVo.TYPE_DAY.equals(type)){
            date = DateUtils.parseDate(startTime,"yyyy-MM");
        }
        list.add(getArrays(tempVoiceCdrList,date,"amongCostTime"));
        return RestResponse.success(list);
    }
    @RequestMapping("/list/api")
    @ResponseBody
    public RestResponse listApiCall(HttpServletRequest request,String type,String startTime,String appId){
        List list = new ArrayList();
        List tempConsumeList = getApiCallList(request,type,appId,startTime);
        Object date = 12;
        if(ConsumeStatisticsVo.TYPE_DAY.equals(type)){
            date = DateUtils.parseDate(startTime,"yyyy-MM");
        }
        list.add(getArrays(tempConsumeList,date,""));
        return RestResponse.success(list);
    }
    @RequestMapping("/list/session")
    @ResponseBody
    public RestResponse listSession(HttpServletRequest request,String type,String startTime,String appId,String callType){
        List list = new ArrayList();
        List tempVoiceCdrList = getVoiceCdrList(request,type,appId,startTime,callType);
        Object date = 12;
        if(ConsumeStatisticsVo.TYPE_DAY.equals(type)){
            date = DateUtils.parseDate(startTime,"yyyy-MM");
        }
        list.add(getArrays(tempVoiceCdrList,date,"amongCall"));
        return RestResponse.success(list);
    }
    /**
     * 获取通话记录（session）的List
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param type 统计类型
     * @return
     */
    private List getApiCallList(HttpServletRequest request,String type,String appId,String startTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/api_call_"+type+"/list?tenantId={1}&appId={2}&startTime={3}";
        Class clazz = ApiCallDay.class;
        if(ConsumeStatisticsVo.TYPE_MONTH.equals(type)){
            clazz = ApiCallMonth.class;
        }
        appId = "-1".equals(appId)?null:appId;
        String tenantId = getCurrentAccount(request).getTenant().getId();
        return (List)RestRequest.buildSecurityRequest(token).getList(uri, clazz,tenantId,appId,startTime).getData();
    }
    /**
     * 获取通话记录（session）的List
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param type 统计类型
     * @return
     */
    private List getVoiceCdrList(HttpServletRequest request,String type,String appId,String startTime,String callType){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/voice_cdr_"+type+"/list?tenantId={1}&appId={2}&startTime={3}&type={4}";
        Class clazz = VoiceCdrDay.class;
        if(ConsumeStatisticsVo.TYPE_MONTH.equals(type)){
            clazz = VoiceCdrMonth.class;
        }
        appId = "-1".equals(appId)?null:appId;
        String tenantId = getCurrentAccount(request).getTenant().getId();
        return (List)RestRequest.buildSecurityRequest(token).getList(uri, clazz,tenantId,appId,startTime,callType).getData();
    }
    /**
     * 获取消费List
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @param type 统计类型
     * @return
     */
    private List getConsumeList(HttpServletRequest request,String type,String appId,String startTime){
        String token = getSecurityToken(request);
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/consume_"+type+"/list?tenantId={1}&appId={2}&startTime={3}";
        Class clazz = ConsumeDay.class;
        if(ConsumeStatisticsVo.TYPE_MONTH.equals(type)){
            clazz = ConsumeMonth.class;
        }
        appId = "-1".equals(appId)?null:appId;
        String tenantId = getCurrentAccount(request).getTenant().getId();
        return (List)RestRequest.buildSecurityRequest(token).getList(uri, clazz,tenantId,appId,startTime).getData();
    }
    private int getLong(Object obj){
        int r = 0;
        if (obj instanceof Date) {
            r = Integer.valueOf(DateUtils.getLastDate((Date)obj).split("-")[2]);
        } else if (obj instanceof Integer) {
            r =Integer.valueOf((Integer)obj);
        }
        return r;
    }
    /**
     * 获取列表数据
     * @param list 待处理的list
     * @return
     */
    private Object[] getArrays(List list,Object date,String type) {
        int leng = getLong(date);
        Object[] list1 = new Object[leng];
        for(int j=0;j<leng;j++){
            list1[j]=0;
        }
        for(int i=0;i<list.size();i++){
            Object obj = list.get(i);
            if(obj instanceof ConsumeMonth){
                list1[((ConsumeMonth)obj).getMonth()-1]= StringUtil.getDecimal(((ConsumeMonth)obj).getAmongAmount().toString(),3);
            }else if(obj instanceof VoiceCdrMonth){
                if("amongCostTime".equals(type)){
                    list1[((VoiceCdrMonth)obj).getMonth()-1]=((VoiceCdrMonth)obj).getAmongCostTime()/60;
                }else if("amongCall".equals(type)) {
                    list1[((VoiceCdrMonth)obj).getMonth()-1]=((VoiceCdrMonth)obj).getAmongCall();
                }
            }else if(obj instanceof ConsumeDay){
                list1[((ConsumeDay)obj).getDay()-1]=StringUtil.getDecimal(((ConsumeDay)obj).getAmongAmount().toString(),3);
            }else if(obj instanceof VoiceCdrDay){
                if("amongCostTime".equals(type)){
                    list1[((VoiceCdrDay)obj).getDay()-1]=((VoiceCdrDay)obj).getAmongCostTime()/60;
                }else if("amongCall".equals(type)) {
                    list1[((VoiceCdrDay)obj).getDay()-1]=((VoiceCdrDay)obj).getAmongCall();
                }
            }else if(obj instanceof ApiCallDay){
                list1[((ApiCallDay)obj).getDay()-1]=((ApiCallDay)obj).getAmongApi();
            }else if(obj instanceof ApiCallMonth){
                list1[((ApiCallMonth) obj).getMonth()-1]=((ApiCallMonth)obj).getAmongApi();
            }
        }
        return list1;
    }

}
