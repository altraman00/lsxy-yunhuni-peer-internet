package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * IVR服务
 * Created by zhangxb on 2016/7/18.
 */
@Controller
@RequestMapping("/console/statistics/specifications/ivr")
public class IVRController  extends AbstractPortalController {
    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/statistics/specifications/ivr");
        return mav;
    }
}
