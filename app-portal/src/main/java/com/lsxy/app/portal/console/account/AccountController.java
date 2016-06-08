package com.lsxy.app.portal.console.account;

import com.alibaba.fastjson.JSONObject;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Tandy on 2016/6/8.
 */
@Controller
@RequestMapping("/console/account")
public class AccountController {



    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView mav = new ModelAndView();

        mav.addObject("a","b");
        mav.setViewName("/console/account/index");
        return mav;
    }

    /**\
     *
     * @return
     */
    @RequestMapping(path = "",method = RequestMethod.POST)
    public RestResponse saveAccount(String xxx){
        RestResponse.failed("errorcode","errormsg");

        return RestResponse.success("");

    }
}
