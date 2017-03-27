package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.*;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.call.center.api.service.AgentOps;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * Created by liuws on 2016/11/14.
 */
@RestController
public class AgentOpsController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(AgentOpsController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private AgentOps agentOps;

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/reject",method = RequestMethod.POST)
    public ApiGatewayResponse reject(HttpServletRequest request, @PathVariable String accountId,
                                     @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                     @Valid @RequestBody RejectTaskInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT REJECT API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        agentOps.reject(getSubaccountId(request),ip,appId,name,dto.getQueueId(),dto.getData());
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/call_out",method = RequestMethod.POST)
    public ApiGatewayResponse call_out(HttpServletRequest request, @PathVariable String accountId,
                                       @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                       @Valid @RequestBody AgentCallOutInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT CALLOUT API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = agentOps.callOut(getSubaccountId(request),ip,appId,name,dto.getFrom(),dto.getTo(),dto.getMaxDialSeconds(),dto.getMaxAnswerSeconds(),dto.getUserData());
        if(logger.isDebugEnabled()){
            logger.debug("AGENT CALLOUT API参数,accountId={},appId={},name={},dto={},callid={}",accountId,appId,name,dto,callId);
        }
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/call_agent",method = RequestMethod.POST)
    public ApiGatewayResponse call_agent(HttpServletRequest request, @PathVariable String accountId,
                                       @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                       @Valid @RequestBody AgentCallAgentInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT CALLAGENT API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = agentOps.callAgent(getSubaccountId(request),ip,appId,name,null,dto.getEnqueue(),null,null);
        if(logger.isDebugEnabled()){
            logger.debug("AGENT CALLAGENT API参数,accountId={},appId={},name={},dto={},callid={}",accountId,appId,name,dto,callId);
        }
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/fwd_agent",method = RequestMethod.POST)
    public ApiGatewayResponse fwd_agent(HttpServletRequest request, @PathVariable String accountId,
                                      @PathVariable String name, @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        //TODO 前转
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/xfer_agent",method = RequestMethod.POST)
    public ApiGatewayResponse xfer_agent(HttpServletRequest request, @PathVariable String accountId,
                                      @PathVariable String name, @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        //TODO 后转
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/xfer_out",method = RequestMethod.POST)
    public ApiGatewayResponse xfer_out(HttpServletRequest request, @PathVariable String accountId,
                                       @PathVariable String name, @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        //TODO 后转外线
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/lsm",method = RequestMethod.POST)
    public ApiGatewayResponse lsm(HttpServletRequest request, @PathVariable String accountId,
                                  @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody SetAgentVoiceModeInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT SETVOICEMODE API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(agentOps.setVoiceMode(getSubaccountId(request),appId,ip,name,dto.getConversationId(),dto.getMode()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/enter",method = RequestMethod.POST)
    public ApiGatewayResponse enter(HttpServletRequest request, @PathVariable String accountId,
                                  @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody AgentEnterConversationInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT ENTER CONVERSATION API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(agentOps.enter(getSubaccountId(request),appId,ip,name,dto.getConversationId(),dto.getMode(),dto.getHolding()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/exit",method = RequestMethod.POST)
    public ApiGatewayResponse exit(HttpServletRequest request, @PathVariable String accountId,
                                  @PathVariable String name, @RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody AgentExitConversationInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT EXIT CONVERSATION API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(agentOps.exit(getSubaccountId(request),appId,ip,name,dto.getConversationId()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/agent/{name}/conversation",method = RequestMethod.GET)
    public ApiGatewayResponse conversations(HttpServletRequest request, @PathVariable String accountId,
                                   @PathVariable String name, @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("AGENT CONVERSATION API参数,accountId={},appId={},name={}",accountId,appId,name);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(agentOps.conversations(getSubaccountId(request),appId,ip,name));
    }
}
