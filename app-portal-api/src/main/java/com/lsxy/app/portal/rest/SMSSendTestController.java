package com.lsxy.app.portal.rest;

import com.lsxy.framework.sms.service.SmsService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/7/7.
 */
@RestController
@RequestMapping("/rest/sms")
public class SMSSendTestController {


    @Autowired
    private SmsService smsService;

    @RequestMapping("/send")
    public RestResponse sendMsg(){
        boolean result = smsService.sendsms("13971068693","短信内容",null);
        return RestResponse.success(true);
    }
}
