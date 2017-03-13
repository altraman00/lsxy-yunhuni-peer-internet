package com.lsxy.app.portal.console.statistics;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.yunhuni.api.app.model.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhangxb on 2017/3/10.
 */
@Controller
@RequestMapping("/console/statistics/msg/")
public class MsgStatisticsController extends AbstractPortalController {
    private static final Logger logger = LoggerFactory.getLogger(MsgStatisticsController.class);

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, ConsumeStatisticsVo consumeStatisticsVo){
        ModelAndView mav = new ModelAndView();
        mav.addObject("appList",getBillAppList(request, App.PRODUCT_MSG).getData());
        mav.addObject("consumeStatisticsVo",consumeStatisticsVo);//时间
        mav.setViewName("/console/statistics/msg/index");
        return mav;
    }
}
