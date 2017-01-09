package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.ChannelCreateInputDTO;
import com.lsxy.app.api.gateway.dto.callcenter.ConditionCreateInputDTO;
import com.lsxy.app.api.gateway.dto.callcenter.ConditionModifyInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.ConfController;
import com.lsxy.area.api.callcenter.ConversationOps;
import com.lsxy.call.center.api.model.Condition;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/11/14.
 */
@RestController
public class ConversationController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ConversationController.class);

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
