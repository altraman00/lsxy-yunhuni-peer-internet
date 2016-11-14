package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.call.center.api.service.ConditionService;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liuws on 2016/11/14.
 */
@RestController
public class ConditionController extends AbstractAPIController {

    @Reference(timeout=3000,check = false,lazy = true)
    private ConditionService conditionService;

}
