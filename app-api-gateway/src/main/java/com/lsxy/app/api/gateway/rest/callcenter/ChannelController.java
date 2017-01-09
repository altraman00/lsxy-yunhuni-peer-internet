package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.ChannelCreateInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.ConfController;
import com.lsxy.call.center.api.service.ConditionService;
import com.lsxy.framework.core.exceptions.api.AppServiceInvalidException;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.service.ChannelService;
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
public class ChannelController extends AbstractAPIController {

    private static final Logger logger = LoggerFactory.getLogger(ChannelController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private ChannelService channelService;

    @Autowired
    private AppService appService;

    @Reference(timeout=3000,check = false,lazy = true)
    private ConditionService conditionService;

    @RequestMapping(value = "/{accountId}/callcenter/channel",method = RequestMethod.POST)
    public ApiGatewayResponse save(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @Valid @RequestBody ChannelCreateInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("创建通道API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        Channel channel = new Channel();
        channel.setRemark(dto.getRemark());
        channel = channelService.save(app.getTenant().getId(),appId,channel);
        Map<String,String> result = new HashMap<>();
        result.put("channelId",channel.getId());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/callcenter/channel/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delete(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("删除通道API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        channelService.delete(app.getTenant().getId(),app.getId(),id);
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{accountId}/callcenter/channel",method = RequestMethod.GET)
    public ApiGatewayResponse channels(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("通道列表API参数,accountId={},appId={},id={}",accountId,appId);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return ApiGatewayResponse.success(channelService.getAll(app.getTenant().getId(),app.getId()));
    }

    @RequestMapping(value = "/{accountId}/callcenter/channel/{id}",method = RequestMethod.GET)
    public ApiGatewayResponse findOne(HttpServletRequest request, @PathVariable String accountId,
                                     @RequestHeader(value = "AppID") String appId,
                                     @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("获取通道API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return ApiGatewayResponse.success(channelService.findOne(app.getTenant().getId(),app.getId(),id));
    }

    @RequestMapping(value = "/{accountId}/callcenter/channel/{channel_id}/condition",method = RequestMethod.GET)
    public ApiGatewayResponse conditions(HttpServletRequest request, @PathVariable String accountId,
                                         @PathVariable String channel_id,
                                         @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("条件列表API参数,accountId={},appId={},channelId={}",accountId,appId,channel_id);
        }
        App app = appService.findById(appId);
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.CallCenter)){
            throw new AppServiceInvalidException();
        }
        return ApiGatewayResponse.success(conditionService.getAll(app.getTenant().getId(),app.getId(),channel_id));
    }
}
