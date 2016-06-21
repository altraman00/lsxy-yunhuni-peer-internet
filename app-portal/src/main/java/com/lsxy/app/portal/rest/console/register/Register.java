package com.lsxy.app.portal.rest.console.register;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by liups on 2016/6/20.
 */
@Controller
@RequestMapping("/register")
public class Register {

    @RequestMapping("/registerPage")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView("register");
        return mav;
    }

}
