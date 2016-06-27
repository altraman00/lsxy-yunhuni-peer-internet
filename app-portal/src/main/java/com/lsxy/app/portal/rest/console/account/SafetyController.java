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
    @RequestMapping(value="/index_psw" )
    public ModelAndView index_psw(HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }
    @RequestMapping(value="/edit_psw" ,method = RequestMethod.POST)
    public ModelAndView edit_psw(HttpServletRequest request ){
        ModelAndView mav = new ModelAndView();
        //todo 修改数据库数据
        int status = 0;
        //todo 0修改成功 -1表示失败
        if(status==0) {
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/safety/edit_psw");
        return mav;
    }
}
