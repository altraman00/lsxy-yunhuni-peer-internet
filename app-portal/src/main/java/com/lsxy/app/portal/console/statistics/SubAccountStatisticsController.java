package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.comm.PortalConstants;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

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
    public ModelAndView index(HttpServletRequest request, @RequestParam(defaultValue = "1") Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize, String monthTime, String dayTime, String appId,String stime){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getAppList(request).getData());
        Date date = new Date();
        if(StringUtil.isEmpty(appId)){
            appId = "all";
        }
        mav.addObject("appId",appId);
        if(StringUtils.isEmpty(monthTime)){
            monthTime = DateUtils.formatDate(new Date(),"yyyy-MM");
        }
        mav.addObject("monthTime",monthTime);
        if(StringUtils.isEmpty(dayTime)){
            dayTime = DateUtils.formatDate(new Date(),"yyyy-MM-dd");
        }
        if(StringUtils.isEmpty(stime)){
            stime = "month";
        }
        mav.addObject("stime",stime);
        mav.addObject("dayTime",dayTime);
        //参数初始化完成
        //获取参数记录
//        String token = getSecurityToken(request);
//        String uri =  PortalConstants.REST_PREFIX_URL  + "/rest/voice_cdr/plist?pageNo={1}&pageSize={2}&type={3}&start={4}&end={5}&appId={6}";
//        RestResponse<Page<VoiceCdr>> result = RestRequest.buildSecurityRequest(token).getPage(uri, VoiceCdr.class, pageNo, pageSize, type, start,end, appId);
//        if(result.isSuccess() && result.getData() != null){
//            List<VoiceCdr> voiceCdrs = result.getData().getResult();
//            formatNum(voiceCdrs);
//        }
//        mav.addObject("sum",sum(request,CallSession.TYPE_VOICE_NOTIFY,map.get("start"),map.get("end"),map.get("appId")).getData());
//        mav.addObject("pageObj",pageObj);
        mav.setViewName("/console/statistics/subaccount/index");
        return mav;
    }
}
