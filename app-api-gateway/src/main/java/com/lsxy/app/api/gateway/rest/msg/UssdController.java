package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendDTO;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendMassDTO;
import com.lsxy.app.api.gateway.rest.msg.dto.UssdSendDTO;
import com.lsxy.app.api.gateway.rest.msg.dto.UssdSendMassDTO;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.service.MsgSendService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2017/3/7.
 */
@RestController
public class UssdController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(UssdController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendService msgSendService;

    @RequestMapping(value = "/{account_id}/msg/ussd/send",method = RequestMethod.POST)
    public ApiGatewayResponse ussdSend(HttpServletRequest request, @Valid @RequestBody UssdSendDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendUssd(ip, appId, subaccountId, dto.getMobile(), dto.getTempId(), dto.getTempArgs());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/msg/ussd/mass/task",method = RequestMethod.POST)
    public ApiGatewayResponse ussdSendMass(HttpServletRequest request, @Valid @RequestBody UssdSendMassDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendUssdMass(ip, appId, subaccountId, dto.getTaskName(),dto.getTempId(),dto.getTempArgs(),dto.getMobiles(),dto.getSendTime());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }


}
