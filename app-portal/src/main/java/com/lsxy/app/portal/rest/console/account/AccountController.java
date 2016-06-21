package com.lsxy.app.portal.rest.console.account;

import com.lsxy.app.portal.rest.security.PortalAuthenticationSuccessHandler;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Tandy on 2016/6/8.
 */
@Controller
@RequestMapping("/console/account")
public class AccountController {

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request){
        if(logger.isDebugEnabled()){
            Object attribute = request.getSession().getAttribute(PortalAuthenticationSuccessHandler.SSO_TOKEN);
            logger.debug("调试登录tocken,{}",attribute);
        }
        ModelAndView mav = new ModelAndView();

        mav.addObject("a","b");
        mav.setViewName("/console/account/index");
        return mav;
    }

    /**
     *
     * @return
     */
    @RequestMapping(path = "",method = RequestMethod.POST)
    public RestResponse saveAccount(String xxx){
        RestResponse.failed("errorcode","errormsg");

        return RestResponse.success("");

    }
}
