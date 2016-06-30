package com.lsxy.app.portal.rest.console.number;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangxb on 2016/6/30.
 * 测试号码绑定处理类
 */
@Controller
@RequestMapping("/console/number/bind")
public class BindController {

    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/console/number/bind/index");
        return mav;
    }
}
