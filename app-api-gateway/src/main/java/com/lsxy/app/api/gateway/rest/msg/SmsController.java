package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendDTO;
import com.lsxy.app.api.gateway.rest.msg.dto.SmsSendMassDTO;
import com.lsxy.app.api.gateway.rest.msg.vo.MsgRequestVO;
import com.lsxy.framework.core.exceptions.api.ExceptionContext;
import com.lsxy.framework.core.exceptions.api.IPNotInWhiteListException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.model.MsgUserRequest;
import com.lsxy.msg.api.service.MsgSendService;
import com.lsxy.msg.api.service.MsgUserRequestService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/3/14.
 */
@RestController
public class SmsController extends AbstractAPIController {
    private static final Logger logger = LoggerFactory.getLogger(SmsController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    MsgSendService msgSendService;
    @Reference(timeout=3000,check = false,lazy = true)
    MsgUserRequestService msgUserRequestService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/msg/sms/send",method = RequestMethod.POST)
    public ApiGatewayResponse smsSend(HttpServletRequest request, @Valid @RequestBody SmsSendDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        appService.enabledService(app.getTenant().getId(),app.getId(), ServiceType.SMS);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }

        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendSms(appId, subaccountId, dto.getMobile(), dto.getTempId(), dto.getTempArgs());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/msg/sms/mass/task",method = RequestMethod.POST)
    public ApiGatewayResponse smsSendMass(HttpServletRequest request, @Valid @RequestBody SmsSendMassDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        appService.enabledService(app.getTenant().getId(),app.getId(), ServiceType.SMS);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        String msgKey = msgSendService.sendSmsMass(appId, subaccountId, dto.getTaskName(),dto.getTempId(),dto.getTempArgs(),dto.getMobiles(),dto.getSendTime());
        Map<String,String> result = new HashMap<>();
        result.put("msgKey", msgKey);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/msg/sms/{msgKey}",method = RequestMethod.GET)
    public ApiGatewayResponse ussdSendMass(HttpServletRequest request, @PathVariable String msgKey, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        appService.enabledService(app.getTenant().getId(),app.getId(), ServiceType.SMS);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        MsgUserRequest msgUserRequest = msgUserRequestService.findByMsgKeyAndSendType(appId,subaccountId,msgKey, ProductCode.msg_sms.name());
        return ApiGatewayResponse.success(new MsgRequestVO(msgUserRequest));
    }

    @RequestMapping(value = "/{account_id}/msg/sms",method = RequestMethod.GET)
    public ApiGatewayResponse ussdSendMass(HttpServletRequest request, @PathVariable String account_id,
                                           @RequestParam(defaultValue = "1") Integer pageNo,
                                           @RequestParam(defaultValue = "10") Integer pageSize) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        appService.enabledService(app.getTenant().getId(),app.getId(), ServiceType.SMS);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        Page<MsgUserRequest> page = msgUserRequestService.findPageBySendTypeForGW(appId, subaccountId, ProductCode.msg_sms.name(), pageNo, pageSize);
        List<MsgUserRequest> result = page.getResult();
        if(result != null && result.size() > 0){
            List<MsgRequestVO> vos = new ArrayList<>();
            for(MsgUserRequest re:result){
                vos.add(new MsgRequestVO(re));
            }
            page.setResult(vos);
        }
        return ApiGatewayResponse.success(page);
    }

}
