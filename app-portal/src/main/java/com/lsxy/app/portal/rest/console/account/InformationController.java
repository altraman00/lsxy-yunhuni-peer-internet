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
@RequestMapping("/console/account/information")
public class InformationController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        InformationEditVo informationEditVo = new InformationEditVo("金融","12313","www.baidu.com","广东省","广州市","天河创意园","020-12345678");
        request.getSession().setAttribute("informationEditVo",informationEditVo);
        mav.addObject("informationEditVo",informationEditVo);
        mav.setViewName("/console/account/information/index");
        return mav;
    }
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, HttpServletResponse response, InformationEditVo informationEditVo ){

        response.setCharacterEncoding("UTF-8");
        ModelAndView mav = new ModelAndView();
        String test = request.getParameter("informationEditVo.business");
        request.getSession().setAttribute("informationEditVo",informationEditVo);
        mav.addObject("informationEditVo",informationEditVo);
        mav.addObject("succes","修改成功"+informationEditVo.toString()+"--"+test);
        mav.setViewName("/console/account/information/index");
        return mav;
    }
}
