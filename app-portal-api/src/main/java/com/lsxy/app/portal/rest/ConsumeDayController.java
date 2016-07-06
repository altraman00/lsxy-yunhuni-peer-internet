package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.model.ConsumeDay;
import com.lsxy.framework.api.consume.service.ConsumeDayService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by zhangxb on 2016/7/6.
 */
@RequestMapping("/rest/consume_day")
@RestController
public class ConsumeDayController extends AbstractRestController {
    @Autowired
    ConsumeDayService consumeDayService;
    @RequestMapping("/list")
    public RestResponse list(String appId,String startTime){
        String userName = getCurrentAccountUserName();
        List<ConsumeDay> list =  consumeDayService.list(userName,appId,startTime);
        return RestResponse.success(list);
    }
}
