package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liups on 2016/11/15.
 */
public class AgentController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/callcenter/agent",method = RequestMethod.POST)
    public ApiGatewayResponse createExtension(HttpServletRequest request, @RequestBody CallCenterAgent agent, @RequestHeader("AppID") String appId) throws YunhuniApiException {
        agent.setAppId(appId);
        App app = appService.findById(appId);
        agent.setTenantId(app.getTenant().getId());
        callCenterAgentService.login(agent);
        return ApiGatewayResponse.success();
    }

}
