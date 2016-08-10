package com.lsxy.app.api.gateway.rest;

import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.framework.mq.TestResetStasticsAccountEvent;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tandy on 16/8/10.
 * 为测试使用的控制器
 */
@Profile(value={"development","local"})
@RestController
public class TestController {

    @Autowired
    private StasticsCounter sc;

    @Autowired
    private MQService mqService;

    @RequestMapping("/test/clean/sa")
    public RestResponse cleanStasticAccount(){
        sc.reset();
        TestResetStasticsAccountEvent trsae = new TestResetStasticsAccountEvent();
        mqService.publish(trsae);
        return RestResponse.success();
    }
}
