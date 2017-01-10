package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.area.api.callcenter.ConversationOps;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by liuws on 2016/11/14.
 */
@RestController
public class ConversationOpsController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationOpsController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private ConversationOps conversationOps;

    @RequestMapping(value = "/{accountId}/callcenter/conversation/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse save(HttpServletRequest request,@PathVariable String accountId, @PathVariable String id,
                                   @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("CONVERSATION DISMISS API参数,accountId={},appId={},dto={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean result = conversationOps.dismiss(ip,appId,id);
        return ApiGatewayResponse.success(result);
    }
}
