package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.RejectTaskInputDTO;
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
            logger.debug("CONVERSATION LIST API参数,accountId={},appId={},name={},dto={}",accountId,appId,name,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        agentOps.reject(ip,appId,name,dto.getQueueId(),dto.getData());
        return ApiGatewayResponse.success(true);
    }
}
