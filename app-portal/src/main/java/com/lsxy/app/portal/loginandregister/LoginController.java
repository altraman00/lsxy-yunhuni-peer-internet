package com.lsxy.app.portal.loginandregister;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 登录处理器
 * Created by liups on 2016/7/6.
 */
@Controller
public class LoginController {
    @RequestMapping("/login")
    public ModelAndView login(){
        String toUrl = "login";
        return new ModelAndView(toUrl);
    }
}
