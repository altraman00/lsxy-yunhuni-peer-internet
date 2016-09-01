package com.lsxy.app.portal.open;

import com.lsxy.framework.sms.service.SmsService;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Tandy on 2016/7/7.
 */
@RestController
@RequestMapping("/open/sms")
@Profile(value = {"local","test","development"})
public class SMSSendTestController {


    @Autowired
    private SmsService smsService;

    @RequestMapping("/send")
        public RestResponse sendMsg(@RequestParam  String mobile){
        String template = "01-portal-test-num-bind.vm";
        Map<String,Object> params = new HashedMap(){{
            put("vc","000000");
        }
        };
        boolean result = smsService.sendsms(mobile,template,params);
        return RestResponse.success(true);
    }
}
