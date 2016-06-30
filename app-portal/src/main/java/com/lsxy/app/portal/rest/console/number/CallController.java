package com.lsxy.app.portal.rest.console.number;

import com.lsxy.app.portal.rest.console.account.InformationController;
import com.lsxy.app.portal.rest.console.account.InformationEditVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangxb on 2016/6/30.
 */
@Controller
@RequestMapping("/console/number/call")
public class CallController {
    private static final Logger logger = LoggerFactory.getLogger(InformationController.class);

    @RequestMapping("/index" )
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();

        mav.setViewName("/console/number/call/index");
        return mav;
    }
}
