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

        InformationEditVo informationEditVo = (InformationEditVo)request.getSession().getAttribute("informationEditVo");
        if(informationEditVo==null) {
            informationEditVo =  new InformationEditVo("金融", "12313", "www.baidu.com", "广东省", "广州市", "天河创意园", "020-12345678");
            request.getSession().setAttribute("informationEditVo",informationEditVo);
        }
        mav.addObject("informationEditVo",informationEditVo);
        mav.setViewName("/console/account/information/index");
        return mav;
    }
    @RequestMapping(value="/edit" ,method = RequestMethod.POST)
    public ModelAndView edit(HttpServletRequest request, InformationEditVo informationEditVo ){
        ModelAndView mav = new ModelAndView();
        //todo 修改数据库数据
        int status = 0;
        //todo 0修改成功 -1表示失败
        if(status==0) {
            request.getSession().setAttribute("informationEditVo", informationEditVo);
            mav.addObject("informationEditVo", informationEditVo);
            mav.addObject("msg", "修改成功！");
        }else{
            mav.addObject("informationEditVo", informationEditVo);
            mav.addObject("msg", "修改失败！");
        }
        mav.setViewName("/console/account/information/index");
        return mav;
    }
}
