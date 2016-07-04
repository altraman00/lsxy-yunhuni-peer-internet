package com.lsxy.app.portal.console.customer;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.console.account.InformationController;
import com.lsxy.framework.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户中心-反馈意见
 * Created by zhangxb on 2016/7/4.
 */
@Controller
@RequestMapping("/console/customer/")
public class FeedbackController  extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/console/customer/index");
        return mav;
    }
}
