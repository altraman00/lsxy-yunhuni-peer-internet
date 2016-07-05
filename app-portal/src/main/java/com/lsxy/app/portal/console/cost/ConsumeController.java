package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 *消费记录控制器
 * Created by liups on 2016/7/1.
 */
@Controller
@RequestMapping("/console/cost/consume")
public class ConsumeController extends AbstractPortalController {
    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    public ModelAndView index(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);

        return new ModelAndView("console/cost/consume/index",model);
    }
}
