package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.callcenter.vo.AgentSkillOptsVO;
import com.lsxy.app.api.gateway.rest.callcenter.vo.AgentSkillVO;
import com.lsxy.app.api.gateway.rest.callcenter.vo.AgentVO;
import com.lsxy.call.center.api.model.CallCenterAgent;
import com.lsxy.call.center.api.service.CallCenterAgentService;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2016/11/15.
 */
@RestController
public class AgentController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(AgentController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private CallCenterAgentService callCenterAgentService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/callcenter/agent",method = RequestMethod.POST)
    public ApiGatewayResponse login(HttpServletRequest request, @RequestBody CallCenterAgent agent, @RequestHeader("AppID") String appId) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        agent.setAppId(appId);
        agent.setTenantId(app.getTenant().getId());
        agent.setSubaccountId(getSubaccountId(request));
        callCenterAgentService.login(agent);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}",method = RequestMethod.DELETE)
    public ApiGatewayResponse logout(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                          @PathVariable("agent_name") String agentName,@RequestParam(value = "force",defaultValue = "false",required = false) Boolean force) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("开始注销座席：{}",agentName);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        callCenterAgentService.logout(app.getTenant().getId(),appId,getSubaccountId(request),agentName,force);
        if(logger.isDebugEnabled()){
            logger.debug("注销座席结束：{}",agentName);
        }
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/keepalive",method = RequestMethod.GET)
    public ApiGatewayResponse keepAlive(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                          @PathVariable("agent_name") String agentName) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        callCenterAgentService.keepAlive(appId,getSubaccountId(request),agentName);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}",method = RequestMethod.GET)
    public ApiGatewayResponse get(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                   @PathVariable("agent_name") String agentName) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        CallCenterAgent agent = callCenterAgentService.get(appId,getSubaccountId(request),agentName);
        AgentVO agentVO = AgentVO.changeCallCenterAgentToAgentVO(agent);
        return ApiGatewayResponse.success(agentVO);
    }


    @RequestMapping(value = "/{account_id}/callcenter/agent",method = RequestMethod.GET)
    public ApiGatewayResponse page(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                   @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                   @RequestParam(defaultValue = "20",required = false)  Integer pageSize) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Page page  = callCenterAgentService.getPage(appId,getSubaccountId(request),pageNo,pageSize);
        List<AgentVO> agentVOs = new ArrayList<>();
        List<CallCenterAgent> result = page.getResult();
        result.stream().forEach(agent -> agentVOs.add(AgentVO.changeCallCenterAgentToAgentVO(agent)));
        page.setResult(agentVOs);
        return ApiGatewayResponse.success(page);
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/extension",method = RequestMethod.POST)
    public ApiGatewayResponse extension(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                        @PathVariable("agent_name") String agentName,@RequestBody Map map) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        String extensionId = (String) map.get("id");
        callCenterAgentService.extension(appId,agentName,extensionId,getSubaccountId(request));
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/state",method = RequestMethod.POST)
    public ApiGatewayResponse state(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                        @PathVariable("agent_name") String agentName,@RequestBody Map map) throws YunhuniApiException {
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        String state = (String) map.get("state");
        //TODO 校验数据有效性
        if(StringUtils.isBlank(state)){
            throw new RequestIllegalArgumentException();
        }
        if(state.equals("busy") || state.equals("away") || state.equals("idle") || state.startsWith("busy/") || state.startsWith("away/")){
            callCenterAgentService.state(appId,getSubaccountId(request),agentName,state);
            return ApiGatewayResponse.success();
        }else{
            throw new RequestIllegalArgumentException();
        }
    }

    @RequestMapping(value = "/{account_id}/callcenter/agent/{agent_name}/skills",method = RequestMethod.POST)
    public ApiGatewayResponse skills(HttpServletRequest request, @RequestHeader("AppID") String appId,
                                    @PathVariable("agent_name") String agentName,@RequestBody AgentSkillOptsVO skillOpts) throws YunhuniApiException {
        //TODO 校验数据有效性
        if(skillOpts.getOpts() == null || skillOpts.getOpts().size() == 0){
            throw new RequestIllegalArgumentException();
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        callCenterAgentService.skills(app.getTenant().getId(),appId,getSubaccountId(request),agentName,skillOpts.getOpts());
        return ApiGatewayResponse.success();
    }

}
