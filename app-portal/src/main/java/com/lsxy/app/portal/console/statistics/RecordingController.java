package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 录音服务
 * Created by zhangxb on 2016/7/18.
 */
@Controller
@RequestMapping("/console/statistics/specifications/recording")
public class RecordingController  extends AbstractPortalController {
    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/statistics/specifications/recording");
        return mav;
    }
}
