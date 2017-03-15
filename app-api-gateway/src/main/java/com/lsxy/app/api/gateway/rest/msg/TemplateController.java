package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.msg.dto.TemplateDTO;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by liups on 2017/3/14.
 */
@RestController
public class TemplateController extends AbstractAPIController {

    @Reference(timeout=3000,check = false,lazy = true)
    MsgTemplateService msgTemplateService;
    @Autowired
    AppService appService;

    @RequestMapping(value = "/{account_id}/msg/template",method = RequestMethod.POST)
    public ApiGatewayResponse createTemplate(HttpServletRequest request, @Valid @RequestBody TemplateDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        App app = appService.findById(appId);
        if(StringUtils.isBlank(dto.getType())){
            dto.setType(ProductCode.msg_sms.name());
        }
        MsgTemplate msgTemplate = new MsgTemplate(app.getTenant().getId(),appId,subaccountId,dto.getName(),dto.getType(),dto.getContent(),dto.getRemark());
        MsgTemplate template = msgTemplateService.createTemplate(msgTemplate);
        return ApiGatewayResponse.success(template);
    }

    @RequestMapping(value = "/{account_id}/msg/template/{templateId}",method = RequestMethod.POST)
    public ApiGatewayResponse updateTemplate(HttpServletRequest request,@PathVariable String templateId, @Valid @RequestBody TemplateDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String ip = WebUtils.getRemoteAddress(request);
        String subaccountId = getSubaccountId(request);
        App app = appService.findById(appId);
        MsgTemplate msgTemplate = new MsgTemplate(app.getTenant().getId(),appId,subaccountId,dto.getName(),dto.getType(),dto.getContent(),dto.getRemark());
        msgTemplate.setTempId(templateId);
        msgTemplate = msgTemplateService.updateMsgTemplate(msgTemplate, true);
        return ApiGatewayResponse.success(msgTemplate);
    }

    @RequestMapping(value = "/{account_id}/msg/template/{templateId}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delTemplate(HttpServletRequest request, @PathVariable String templateId, @PathVariable String account_id) throws YunhuniApiException, InvocationTargetException, IllegalAccessException {
        String appId = request.getHeader("AppID");
        String subaccountId = getSubaccountId(request);
        msgTemplateService.deleteMsgTemplate(appId,subaccountId,templateId,true);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/msg/template/{templateId}",method = RequestMethod.GET)
    public ApiGatewayResponse getTemplate(HttpServletRequest request, @PathVariable String templateId, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String subaccountId = getSubaccountId(request);
        MsgTemplate temp = msgTemplateService.findByTempId(appId, subaccountId, templateId, true);
        return ApiGatewayResponse.success(temp);
    }

    @RequestMapping(value = "/{account_id}/msg/template",method = RequestMethod.GET)
    public ApiGatewayResponse getTemplatePage(HttpServletRequest request, @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                       @RequestParam(defaultValue = "20",required = false)  Integer pageSize, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        String subaccountId = getSubaccountId(request);
        Page<MsgTemplate> page = msgTemplateService.getPageForGW(appId, subaccountId, pageNo, pageSize);
        return ApiGatewayResponse.success(page);
    }

}
