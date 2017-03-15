package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendDTO;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendMassDTO;
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
 * Created by liups on 2017/3/14.
 */
@RestController
public class SmsController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendService msgSendService;


    @RequestMapping(value = "/{account_id}/msg/sms/send",method = RequestMethod.POST)
    public ApiGatewayResponse smsSend(HttpServletRequest request, @Valid @RequestBody SmsSendDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendSms(ip, appId, subaccountId, dto.getMobile(), dto.getTempId(), dto.getTempArgs());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/msg/sms/mass/task",method = RequestMethod.POST)
    public ApiGatewayResponse smsSendMass(HttpServletRequest request, @Valid @RequestBody SmsSendMassDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendSmsMass(ip, appId, subaccountId, dto.getTaskName(),dto.getTempId(),dto.getTempArgs(),dto.getMobiles(),dto.getSendTime());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }

}
