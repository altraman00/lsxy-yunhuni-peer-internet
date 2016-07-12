package com.lsxy.app.portal.rest.test;

import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.sms.service.SmsService;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Tandy on 2016/7/7.
 */
@RestController
@RequestMapping("/rest/cache")
public class CacheTestController {


    @Autowired
    private AccountService accountService;

    @RequestMapping("/get/{id}")
    public RestResponse sendMsg(@PathVariable  String id){
        return RestResponse.success(id);
    }
}
