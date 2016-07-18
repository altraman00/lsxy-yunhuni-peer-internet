package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 语音呼叫
 * Created by zhangxb on 2016/7/18.
 */
@Controller
@RequestMapping("/console/statistics/specifications/")
public class VoiceCallController  extends AbstractPortalController {
    @RequestMapping("")
    public ModelAndView index(HttpServletRequest request){
        ModelAndView mav = new ModelAndView();
        mav.setViewName("/console/statistics/specifications/call");
        return mav;
    }
}
