package com.lsxy.app.portal.comm;

import com.lsxy.framework.mail.MailConfigNotEnabledException;
import com.lsxy.framework.mail.MailService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tandy on 2016/6/7.
 */
@Controller

public class HaHaController {

    @Autowired
    private MailService mailService;

    @RequestMapping("/haha")
    public ModelAndView goHaHa(){
        ModelAndView mav = new ModelAndView("/haha");

        return mav;
    }

    @RequestMapping(path = "/postxx",method = RequestMethod.POST)
    public String postChinese(String xx){
        return xx;
    }

    @RequestMapping(path="/testmail")
    @ResponseBody
    public RestResponse testMail() throws MailConfigNotEnabledException {
        String title = "哈哈哈哈哈哈哈";
        String url = "http://www.baidu.com";
        Map<String, String> params = new HashMap<String, String>() {
            {
                put("url", url);
            }
        };
        String mailto = "51562066@qq.com";
        mailService.send(title,mailto,"这是邮件内容，测试使用的");
        return RestResponse.success("mail to");
    }
}
