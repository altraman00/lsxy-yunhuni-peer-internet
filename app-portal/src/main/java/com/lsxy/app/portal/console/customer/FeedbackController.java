package com.lsxy.app.portal.console.customer;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.api.customer.model.Feedback;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.web.rest.RestRequest;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 客户中心-反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@Controller
@RequestMapping("/console/customer")
public class FeedbackController  extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(FeedbackController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");

    /**
     * 反馈意见首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/customer/index");
        return mav;
    }

    /**
     * 保存反馈意见
     * @param request
     * @param content 反馈内容
     * @return
     */
    @RequestMapping("/edit")
    public ModelAndView edit(HttpServletRequest request,String content){
        ModelAndView mav = new ModelAndView();
        RestResponse<Feedback> restResponse = save(request,content,"0");
        mav.addObject("msg","感谢你的反馈意见！");
        mav.setViewName("/console/customer/index");
        return mav;
    }

    /**
     * 保存意见反馈信息
     * @param request
     * @param content 反馈内容
     * @param status 意见状态，未处理为0，处理为1
     * @return
     */
    private RestResponse save(HttpServletRequest request,String content,String status){
        String token = getSecurityToken(request);
        String uri = restPrefixUrl +   "/rest/customer/feedback/save";
        Map map = new HashMap();
        map.put("content",content);
        map.put("status",status);
        return  RestRequest.buildSecurityRequest(token).post(uri,map, Feedback.class);
    }
}
