package com.lsxy.app.portal.rest.console.home;

import com.lsxy.app.portal.rest.base.AbstractPortalController;
import com.lsxy.app.portal.rest.comm.PortalConstants;
import com.lsxy.app.portal.rest.console.home.vo.HomeVOMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录后的首页
 * Created by liups on 2016/6/27.
 */
@Controller
@RequestMapping("/console/home")
public class HomeController extends AbstractPortalController{
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @RequestMapping(value = "/index",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request){
        String token = getToken(request);
        HomeVOMaker.getHomeVO(token);
        return new ModelAndView("console/home/index");
    }

}
