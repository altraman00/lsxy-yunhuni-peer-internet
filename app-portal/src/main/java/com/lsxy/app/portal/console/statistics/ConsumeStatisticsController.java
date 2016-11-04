package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.yunhuni.api.statistics.model.ConsumeDay;
import com.lsxy.yunhuni.api.statistics.model.ConsumeMonth;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
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
import java.util.*;

/**
 * 消费统计
 * Created by zhangxb on 2016/7/6.
 */
@Controller
@RequestMapping("/console/statistics/consume/")
public class ConsumeStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(ConsumeStatisticsController.class);
    /**
     * 消费统计首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        mav.addObject("consumeStatisticsVo",consumeStatisticsVo);//时间
        mav.setViewName("/console/statistics/consume/index");
        return mav;
    }



    /**
     * 异步分页信息
     * @param request
     * @param consumeStatisticsVo
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/page_list")
    @ResponseBody
    public RestResponse pageList(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo,
                         @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20")Integer pageSize){
        List list = ((Page)getPageList( request, consumeStatisticsVo, pageNo, pageSize).getData()).getResult();
        return RestResponse.success(list);
    }

    /**
     * 异步获取图表显示信息
     * @param request
     * @param consumeStatisticsVo
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public RestResponse list(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo){
        List list = new ArrayList();
        if(ConsumeStatisticsVo.TYPE_DAY.equals(consumeStatisticsVo.getType())){//日统计比较
            Map map1 = getConsumeDayList(request,consumeStatisticsVo.getAppId(),consumeStatisticsVo.getStartTime());
            list.add(map1);
            if(consumeStatisticsVo.getEndTime().length()>0){//有对比时间
                Map map2 = getConsumeDayList(request,consumeStatisticsVo.getAppId(),consumeStatisticsVo.getEndTime());
                list.add(map2);
            }
        }else{//月统计
            Map map1 = getConsumeMonthList(request,consumeStatisticsVo.getAppId(),consumeStatisticsVo.getStartTime());
            list.add(map1);
            if(consumeStatisticsVo.getEndTime().length()>0){//有对比时间
                Map map2 = getConsumeMonthList(request,consumeStatisticsVo.getAppId(),consumeStatisticsVo.getEndTime());
                list.add(map2);
            }
        }
        return RestResponse.success(list);
    }

    /**
     * 获取消费日统计的图表显示信息
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @return
     */
    private Map getConsumeDayList(HttpServletRequest request,String appId,String startTime){
        String token = getSecurityToken(request);
        appId = "-1".equals(appId)?null:appId;
        String tenantId = getCurrentAccount(request).getTenant().getId();
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/consume_day/list?tenantId={1}&appId={2}&startTime={3}";
        RestResponse<List<ConsumeDay>> restResponse =  RestRequest.buildSecurityRequest(token).getList(uri, ConsumeDay.class,tenantId,appId,startTime);
        List<ConsumeDay> list =  restResponse.getData();
        Map map = new HashMap();
        map.put("name",DateUtils.formatDate(DateUtils.parseDate(startTime,"yyyy-MM"),"MM月"));
        map.put("type","line");
        map.put("data",getArrays(list,DateUtils.parseDate(startTime,"yyyy-MM")));
        return map;
    }
    /**
     * 获取消费月统计的图表显示信息
     * @param request
     * @param appId 应用id
     * @param startTime 开始时间
     * @return
     */
    private Map getConsumeMonthList(HttpServletRequest request,String appId,String startTime){
        String token = getSecurityToken(request);
        appId = "-1".equals(appId)?null:appId;
        String tenantId = getCurrentAccount(request).getTenant().getId();
        String uri = PortalConstants.REST_PREFIX_URL  +   "/rest/consume_month/list?tenantId={1}&appId={2}&startTime={3}";
        RestResponse<List<ConsumeMonth>> restResponse =  RestRequest.buildSecurityRequest(token).getList(uri, ConsumeMonth.class,tenantId,appId,startTime);
        List<ConsumeMonth> list =  restResponse.getData();
        Map map = new HashMap();
        map.put("name",startTime+"年");
        map.put("type","line");
        map.put("data",getArrays(list,12));
        return map;
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
    private String[] getArrays(List list,Object date) {
        int leng = getLong(date);
        String[] list1 = new String[leng];
        for(int j=0;j<leng;j++){
            list1[j]="0";
        }
        for(int i=0;i<list.size();i++){
            Object obj = list.get(i);
            if(obj instanceof ConsumeMonth){
                list1[((ConsumeMonth)obj).getMonth()-1]=StringUtil.getDecimal(((ConsumeMonth)obj).getAmongAmount().toString(),3);
            }else if(obj instanceof ConsumeDay){
                list1[((ConsumeDay)obj).getDay()-1]=StringUtil.getDecimal(((ConsumeDay)obj).getAmongAmount().toString(),3);
            }
        }
        return list1;
    }

    /**
     * 获取消费统计的分页信息
     * @param request
     * @param consumeStatisticsVo 消费统计VO对象
     * @param pageNo 第几页
     * @param pageSize 一页多少数据
     * @return
     */
    private RestResponse getPageList(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo,Integer pageNo,Integer pageSize){
        String token = getSecurityToken(request);
        String compare = "";
        if(StringUtil.isNotEmpty(consumeStatisticsVo.getEndTime())){
            compare="compare_";
        }
        String uri = PortalConstants.REST_PREFIX_URL  + "/rest/consume_"+consumeStatisticsVo.getType()+"/"+compare+"page?tenantId={1}&appId={2}&type={3}&startTime={4}&endTime={5}&pageNo={6}&pageSize={7}";
        Class clazz = ConsumeMonth.class;
        if(ConsumeStatisticsVo.TYPE_DAY.equals(consumeStatisticsVo.getType())){
            clazz = ConsumeDay.class;
        }
        String appId = "-1".equals(consumeStatisticsVo.getAppId())?null:consumeStatisticsVo.getAppId();
        String tenantId = getCurrentAccount(request).getTenant().getId();
        return RestRequest.buildSecurityRequest(token).getPage(uri,clazz ,tenantId,appId,
                null,consumeStatisticsVo.getStartTime(),consumeStatisticsVo.getEndTime(),pageNo,pageSize);
    }
}
