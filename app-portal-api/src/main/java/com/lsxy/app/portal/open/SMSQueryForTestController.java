package com.lsxy.app.portal.open;

import com.lsxy.framework.api.sms.model.SMSSendLog;
import com.lsxy.framework.api.sms.service.SMSSendLogService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/7/12.
 */
@RequestMapping("/open/sms")
@Profile({"test","local","development"})
@RestController
public class SMSQueryForTestController {
    @Autowired
    private SMSSendLogService smsSendLogService;

    @RequestMapping(path="/{mobile}")
    public RestResponse<Page<SMSSendLog>> querySms(@PathVariable String mobile){
            Page<SMSSendLog> result =  smsSendLogService.findByMobile(mobile,1,10);
        return RestResponse.success(result);
    }
}
