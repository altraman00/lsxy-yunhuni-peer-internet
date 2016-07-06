package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.console.customer.FeedbackController;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
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
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.addObject("ConsumeStatisticsVo",getAppList(request).getData());
        mav.addObject("initTime",DateUtils.formatDate(new Date(),"yyyy-MM"));//时间
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
    public List index(HttpServletRequest request,String appId,String startTime,String endTime,String type){
        RestResponse<List<ConsumeDay>> restResponse =  getList(request,appId,startTime);
        List<ConsumeDay> list =  restResponse.getData();
        return list;
    }
    private RestResponse getList(HttpServletRequest request,String appId,String startTime){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/consume_day/list?appId={1}&startTime={2}";
        return RestRequest.buildSecurityRequest(token).getList(uri, ConsumeDay.class,appId,startTime);
    }
}
