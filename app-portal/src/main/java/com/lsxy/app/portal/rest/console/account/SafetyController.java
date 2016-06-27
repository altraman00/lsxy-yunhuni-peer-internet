package com.lsxy.app.portal.rest.console.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Administrator on 2016/6/24.
 */
@Controller
@RequestMapping("/console/account/safety")
public class SafetyController {
    private static final Logger logger = LoggerFactory.getLogger(SafetyController.class);

    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/console/account/safety/index");
        return mav;
    }
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/account/safety/index");
        return mav;
    }
}
