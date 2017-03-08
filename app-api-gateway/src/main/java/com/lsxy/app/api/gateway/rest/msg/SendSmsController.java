package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.msg.api.service.MsgSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by liups on 2017/3/7.
 */
@RestController
public class SendSmsController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(SendSmsController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendService msgSendService;

}
