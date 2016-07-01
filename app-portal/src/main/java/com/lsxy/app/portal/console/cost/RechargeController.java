package com.lsxy.app.portal.console.cost;

import com.lsxy.app.portal.base.AbstractPortalController;
import com.lsxy.app.portal.security.AvoidDuplicateSubmission;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 充值控制器
 * Created by liups on 2016/7/1.
 */
@Controller
@RequestMapping("/console/cost/recharge")
public class RechargeController extends AbstractPortalController {
    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "",method = RequestMethod.GET)
    @AvoidDuplicateSubmission(needSaveToken = true) //需要生成防重token的方法用这个
    public ModelAndView index(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);

        return new ModelAndView("console/cost/recharge/index",model);
    }

    /**
     * 返回消费记录
     * @param request
     * @return
     */
    @RequestMapping(value = "sure",method = RequestMethod.POST)
    @AvoidDuplicateSubmission(needRemoveToken = true) //需要检验token防止重复提交的方法用这个
    public ModelAndView sure(HttpServletRequest request){
        Map<String,Object> model = new HashMap<>();
        String token = getSecurityToken(request);

        return new ModelAndView("console/cost/recharge/sure",model);
    }

}
