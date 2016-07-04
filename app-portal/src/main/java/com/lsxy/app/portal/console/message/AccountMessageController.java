package com.lsxy.app.portal.console.message;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.framework.config.SystemConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/4.
 */
@Controller
@RequestMapping("/console/message/account_message")
public class AccountMessageController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(AccountMessageController.class);
    private String restPrefixUrl = SystemConfig.getProperty("portal.rest.api.url");

    /**
     * 用户消息首页
     * @param request
     * @return
     */
    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/message/index");
        return mav;
    }
}
