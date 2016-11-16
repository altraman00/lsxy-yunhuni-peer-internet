package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

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
    public ApiGatewayResponse login(HttpServletRequest request, @RequestBody CallCenterAgent agent, @RequestHeader("AppID") String appId) throws YunhuniApiException {
        agent.setAppId(appId);
        App app = appService.findById(appId);
        agent.setTenantId(app.getTenant().getId());
        callCenterAgentService.login(agent);
        return ApiGatewayResponse.success();
    }


    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}",method = RequestMethod.DELETE)
    public ApiGatewayResponse logout(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                          @PathVariable("agent_name") String agentName,@RequestParam("channel") String channel,@RequestParam("force") boolean force) throws YunhuniApiException {
        App app = appService.findById(appId);
        callCenterAgentService.logout(app.getTenant().getId(),appId,channel,agentName,force);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/keepalive",method = RequestMethod.GET)
    public ApiGatewayResponse keepAlive(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                          @PathVariable("agent_name") String agentName,@RequestParam("channel") String channel) throws YunhuniApiException {
        callCenterAgentService.keepAlive(appId,channel,agentName);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}",method = RequestMethod.GET)
    public ApiGatewayResponse get(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                   @PathVariable("agent_name") String agentName,@RequestParam("channel") String channel) throws YunhuniApiException {
        CallCenterAgent agent = callCenterAgentService.get(appId,channel,agentName);
        return ApiGatewayResponse.success(agent);
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent",method = RequestMethod.GET)
    public ApiGatewayResponse page(HttpServletRequest request, @RequestHeader("AppID") String appId,@RequestParam("channel") String channel,
                                   @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                   @RequestParam(defaultValue = "20",required = false)  Integer pageSize) throws YunhuniApiException {
        Page page  = callCenterAgentService.getPage(appId,channel,pageNo,pageSize);
        return ApiGatewayResponse.success(page);
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/extension",method = RequestMethod.POST)
    public ApiGatewayResponse extension(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                        @PathVariable("agent_name") String agentName,@RequestBody Map map) throws YunhuniApiException {
        String extensionId = (String) map.get("id");
        String channel = (String) map.get("channel");
        callCenterAgentService.extension(appId,channel,agentName,extensionId);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/state",method = RequestMethod.POST)
    public ApiGatewayResponse state(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                        @PathVariable("agent_name") String agentName,@RequestBody Map map) throws YunhuniApiException {
        String state = (String) map.get("state");
        String channel = (String) map.get("channel");
        //TODO 校验数据有效性
        if(StringUtils.isBlank(state)){
            throw new RuntimeException("状态不能为空");
        }
        callCenterAgentService.state(appId,channel,agentName,state);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/skills",method = RequestMethod.POST)
    public ApiGatewayResponse skills(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                    @PathVariable("agent_name") String agentName,@RequestBody Map map) throws YunhuniApiException {
        String state = (String) map.get("state");
        String channel = (String) map.get("channel");
        //TODO 校验数据有效性
        if(StringUtils.isBlank(state)){
            throw new RuntimeException("状态不能为空");
        }
        callCenterAgentService.state(appId,channel,agentName,state);
        return ApiGatewayResponse.success();
    }

}
