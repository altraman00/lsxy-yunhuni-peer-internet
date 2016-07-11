package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by liups on 2016/7/11.
 */
@Controller
@RequestMapping("/console/cost/bill_month")
public class BillMonthController extends AbstractPortalController {

    public ModelAndView get(){
        return new ModelAndView("console/cost/bill/bill_month");
    }
}
