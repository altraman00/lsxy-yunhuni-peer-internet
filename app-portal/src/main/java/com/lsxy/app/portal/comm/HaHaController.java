package com.lsxy.app.portal.comm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Tandy on 2016/6/7.
 */
@Controller

public class HaHaController {
    @RequestMapping("/haha")
    public ModelAndView goHaHa(){
        ModelAndView mav = new ModelAndView("/haha");

        return mav;
    }

    @RequestMapping(path = "/postxx",method = RequestMethod.POST)
    public String postChinese(String xx){
        return xx;
    }


}
