package com.lsxy.app.api.gateway.rest.msg;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.msg.dto.TemplateDTO;
import com.lsxy.app.api.gateway.rest.msg.vo.TemplateVO;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.IPNotInWhiteListException;
import com.lsxy.framework.core.exceptions.api.RequestIllegalArgumentException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.utils.WebUtils;
import com.lsxy.msg.api.model.MsgTemplate;
import com.lsxy.msg.api.service.MsgTemplateService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
        if(StringUtils.isBlank(dto.getType())){
            dto.setType(ProductCode.msg_sms.name());
        }
        ServiceType serviceType;
        if(ProductCode.msg_sms.name().equals(dto.getType())){
            serviceType = ServiceType.SMS;
        }else if(ProductCode.msg_ussd.name().equals(dto.getType())){
            serviceType = ServiceType.USSD;
        }else{
            throw new RequestIllegalArgumentException();
        }

        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),app.getId(), serviceType)){
            throw new AppServiceInvalidException();
        }

        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        MsgTemplate msgTemplate = new MsgTemplate(app.getTenant().getId(),appId,subaccountId,dto.getName(),dto.getType(),dto.getContent(),dto.getRemark());
        MsgTemplate template = msgTemplateService.createTemplate(msgTemplate);
        return ApiGatewayResponse.success(new TemplateVO(template));
    }

    @RequestMapping(value = "/{account_id}/msg/template/{tempId}",method = RequestMethod.POST)
    public ApiGatewayResponse updateTemplate(HttpServletRequest request,@PathVariable String tempId, @Valid @RequestBody TemplateDTO dto, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        MsgTemplate msgTemplate = new MsgTemplate(app.getTenant().getId(),appId,subaccountId,dto.getName(),dto.getType(),dto.getContent(),dto.getRemark());
        msgTemplate.setTempId(tempId);
        msgTemplate = msgTemplateService.updateMsgTemplate(msgTemplate, true);
        return ApiGatewayResponse.success(new TemplateVO(msgTemplate));
    }

    @RequestMapping(value = "/{account_id}/msg/template/{tempId}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delTemplate(HttpServletRequest request, @PathVariable String tempId, @PathVariable String account_id) throws YunhuniApiException, InvocationTargetException, IllegalAccessException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        msgTemplateService.deleteMsgTemplate(appId,subaccountId,tempId,true);
        return ApiGatewayResponse.success();
    }

    @RequestMapping(value = "/{account_id}/msg/template/{tempId}",method = RequestMethod.GET)
    public ApiGatewayResponse getTemplate(HttpServletRequest request, @PathVariable String tempId, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        if("1001".equals(tempId)){
            return ApiGatewayResponse.success();
        }
        MsgTemplate temp = msgTemplateService.findByTempId(appId, subaccountId, tempId, true);
        return ApiGatewayResponse.success(new TemplateVO(temp));
    }

    @RequestMapping(value = "/{account_id}/msg/template",method = RequestMethod.GET)
    public ApiGatewayResponse getTemplatePage(HttpServletRequest request, @RequestParam(defaultValue = "1",required = false) Integer  pageNo,
                                       @RequestParam(defaultValue = "20",required = false)  Integer pageSize, @PathVariable String account_id) throws YunhuniApiException {
        String appId = request.getHeader("AppID");
        App app = appService.findById(appId);
        String ip = WebUtils.getRemoteAddress(request);
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException();
            }
        }
        String subaccountId = getSubaccountId(request);
        Page<MsgTemplate> page = msgTemplateService.getPageForGW(appId, subaccountId, pageNo, pageSize);
        List<MsgTemplate> result = page.getResult();
        if(result != null && result.size() >0){
            List<TemplateVO> vos = new ArrayList<>();
            for(MsgTemplate msgTemplate:result ){
                vos.add(new TemplateVO(msgTemplate));
            }
            page.setResult(vos);
        }
        return ApiGatewayResponse.success(page);
    }

}
