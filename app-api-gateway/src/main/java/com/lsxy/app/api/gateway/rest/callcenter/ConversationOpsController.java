package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.InviteAgentInputDTO;
import com.lsxy.app.api.gateway.dto.callcenter.InviteOutInputDTO;
import com.lsxy.app.api.gateway.dto.callcenter.SetVoiceModeInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.api.service.ConversationOps;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.exceptions.api.AgentNotExistException;
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
public class ConversationOpsController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationOpsController.class);

    @Reference(timeout=30000,check = false,lazy = true)
    private ConversationOps conversationOps;

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterConversationService callCenterConversationService;

    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse dismiss(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                   @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION DISMISS API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean result = conversationOps.dismiss(getSubaccountId(request),ip,appId,id);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}/agent/{name}/lsm",method = RequestMethod.POST)
    public ApiGatewayResponse lsm(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                  @PathVariable String name,@RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody SetVoiceModeInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION setVoiceMode API参数,accountId={},appId={},id={},name={},dto={}",accountId,appId,id,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        String agentId = callCenterAgentService.getId(appId,getSubaccountId(request),name);
        if(agentId == null){
            throw new AgentNotExistException();
        }
        boolean result = conversationOps.setVoiceMode(getSubaccountId(request),ip,appId,id,agentId,dto.getMode());
        return ApiGatewayResponse.success(result);
    }


    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}/invite_agent",method = RequestMethod.POST)
    public ApiGatewayResponse invite_agent(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                  @RequestHeader(value = "AppID") String appId,
                                  @Valid @RequestBody InviteAgentInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION INVITE_AGENT API参数,accountId={},appId={},id={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(conversationOps.inviteAgent(getSubaccountId(request),ip,appId,id,dto.getEnqueue(),dto.getMode()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}/invite_out",method = RequestMethod.POST)
    public ApiGatewayResponse invite_out(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                           @RequestHeader(value = "AppID") String appId,
                                           @Valid @RequestBody InviteOutInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION INVITE_OUT API参数,accountId={},appId={},id={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        return ApiGatewayResponse.success(conversationOps.inviteOut(getSubaccountId(request),ip,appId,id,dto.getFrom(),
                dto.getTo(),dto.getMaxDialDuration(),dto.getMaxCallDuration(),dto.getMode()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}",method = RequestMethod.GET)
    public ApiGatewayResponse getConversationDetail(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                         @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION GET API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);

        return ApiGatewayResponse.success(callCenterConversationService.detail(getSubaccountId(request),ip,appId,id));
    }

    @RequestMapping(value = "/{accountId}/callcenter/conversation",method = RequestMethod.GET)
    public ApiGatewayResponse pagelist(HttpServletRequest request,@PathVariable String accountId,
                                       @RequestParam(required = false,defaultValue = "1") int page,
                                       @RequestParam(required = false,defaultValue = "10") int size,
                                                    @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION LIST API参数,accountId={},appId={},id={}",accountId,appId);
        }
        String ip = WebUtils.getRemoteAddress(request);

        return ApiGatewayResponse.success(callCenterConversationService.conversationPageList(getSubaccountId(request),ip,appId,page,size));
    }
}
