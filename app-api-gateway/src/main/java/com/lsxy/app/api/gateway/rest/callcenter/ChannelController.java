package com.lsxy.app.api.gateway.rest.callcenter;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.callcenter.ChannelCreateInputDTO;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.app.api.gateway.rest.AbstractAPIController;
import com.lsxy.app.api.gateway.rest.ConfController;
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
                                   @Valid @RequestBody ChannelCreateInputDTO dto){
        if(logger.isDebugEnabled()){
            logger.debug("创建通道API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        App app = appService.findById(appId);
        Channel channel = new Channel();
        channel.setTenantId(app.getTenant().getId());
        channel.setAppId(app.getId());
        channel.setRemark(dto.getRemark());
        channelService.save(channel);
        return ApiGatewayResponse.success(true);
    }
}
