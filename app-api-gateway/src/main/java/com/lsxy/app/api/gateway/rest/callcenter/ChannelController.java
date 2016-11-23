package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.ChannelCreateInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.ConfController;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.call.center.api.model.Channel;
import com.lsxy.call.center.api.service.ChannelService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
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

    private static final Logger logger = LoggerFactory.getLogger(ConfController.class);

    @Reference(timeout=3000,check = false,lazy = true)
    private ChannelService channelService;

    @Autowired
    private AppService appService;

    @RequestMapping(value = "/{account_id}/callcenter/channel",method = RequestMethod.POST)
    public ApiGatewayResponse save(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @Valid @RequestBody ChannelCreateInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("创建通道API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        App app = appService.findById(appId);
        Channel channel = new Channel();
        channel.setTenantId(app.getTenant().getId());
        channel.setAppId(app.getId());
        channel.setRemark(dto.getRemark());
        channel = channelService.save(channel);
        Map<String,String> result = new HashMap<>();
        result.put("channelId",channel.getId());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{account_id}/callcenter/channel/{id}",method = RequestMethod.DELETE)
    public ApiGatewayResponse delete(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId,
                                   @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("删除通道API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        channelService.delete(app.getTenant().getId(),app.getId(),id);
        return ApiGatewayResponse.success(true);
    }

    @RequestMapping(value = "/{account_id}/callcenter/channel",method = RequestMethod.GET)
    public ApiGatewayResponse channels(HttpServletRequest request, @PathVariable String accountId,
                                   @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("通道列表API参数,accountId={},appId={},id={}",accountId,appId);
        }
        App app = appService.findById(appId);
        return ApiGatewayResponse.success(channelService.getAll(app.getTenant().getId(),app.getId()));
    }

    @RequestMapping(value = "/{account_id}/callcenter/channel/{id}",method = RequestMethod.GET)
    public ApiGatewayResponse findOne(HttpServletRequest request, @PathVariable String accountId,
                                     @RequestHeader(value = "AppID") String appId,
                                     @PathVariable String id) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("获取通道API参数,accountId={},appId={},id={}",accountId,appId,id);
        }
        App app = appService.findById(appId);
        return ApiGatewayResponse.success(channelService.findOne(app.getTenant().getId(),app.getId(),id));
    }
}
