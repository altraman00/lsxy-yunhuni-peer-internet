package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.console.customer.FeedbackController;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yuhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 消费统计
 * Created by zhangxb on 2016/7/6.
 */
@Controller
@RequestMapping("/console/statistics/consume/")
public class ConsumeStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");
    /**
     * 消费统计首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo,Integer pageNo,Integer pageSize){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        if(consumeStatisticsVo==null){
            consumeStatisticsVo = new ConsumeStatisticsVo("month",DateUtils.formatDate(new Date(),"yyyy-MM"),"","0");
        }
        mav.addObject("appList",getAppList(request).getData());
        mav.addObject("consumeStatisticsVo",consumeStatisticsVo);//时间
        mav.addObject("pageObj",getPageList(request,consumeStatisticsVo,pageNo,pageSize));
        mav.setViewName("/console/statistics/consume/index");
        return mav;
    }

    /**
     * 获取租户下的全部应用
     * @param request
     * @return
     */
    private RestResponse getAppList(HttpServletRequest request){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/app/list";
        return RestRequest.buildSecurityRequest(token).getList(uri, App.class);
    }
    @RequestMapping("/list")
    @ResponseBody
    public List list(HttpServletRequest request, String appId, String startTime, String endTime, String type){
        RestResponse<List<ConsumeDay>> restResponse =  getList(request,appId,startTime);
        List<ConsumeDay> list =  restResponse.getData();
        return list;
    }
    private RestResponse getList(HttpServletRequest request,String appId,String startTime){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/consume_day/list?appId={1}&startTime={2}";
        return RestRequest.buildSecurityRequest(token).getList(uri, ConsumeDay.class,appId,startTime);
    }
    private RestResponse getPageList(HttpServletRequest request,ConsumeStatisticsVo consumeStatisticsVo,Integer pageNo,Integer pageSize){
        String token = getSecurityToken(request);
        if(pageNo==null){pageNo=1;}
        if(pageSize==null){pageSize=20;}
        if(consumeStatisticsVo.getEndTime().length()==0){consumeStatisticsVo.setEndTime(consumeStatisticsVo.getStartTime());}
        String uri = restPrefixUrl +   "/rest/consume_day/page?appId={1}&startTime={2}&endTime={3}&pageNo={4}&pageSize={5}";
        return RestRequest.buildSecurityRequest(token).getPage(uri, ConsumeDay.class,consumeStatisticsVo.getAppId(),consumeStatisticsVo.getStartTime(),consumeStatisticsVo.getEndTime(),pageNo,pageSize);
    }
}
