package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.app.api.gateway.dto.*;
import com.lsxy.app.api.gateway.response.ApiGatewayResponse;
import com.lsxy.area.api.ConfService;
import com.lsxy.area.api.exceptions.YunhuniApiException;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liuws on 2016/8/24.
 */
@RestController
public class ConfController extends AbstractAPIController{
    private static final Logger logger = LoggerFactory.getLogger(ConfController.class);

    @Reference(timeout=3000)
    private ConfService confService;

    @RequestMapping(value = "/{accountId}/conf/create",method = RequestMethod.POST)
    public ApiGatewayResponse create(HttpServletRequest request, @PathVariable String accountId,
                                    @RequestHeader(value = "AppID") String appId,
                                    @RequestBody ConfCreateInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("创建会议API参数,accountId={},appId={},dto={}",accountId,appId,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = confService.create(ip,appId,dto.getMaxDuration(),dto.getMaxParts(),
                dto.getRecording(),dto.getAutoHangup(),dto.getBgmFile(),dto.getUserData());
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return ApiGatewayResponse.success(result);
    }


    @RequestMapping(value = "/{accountId}/conf/{id}/dismiss",method = RequestMethod.POST)
    public ApiGatewayResponse dismiss(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                               @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("解散会议API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean result = confService.dismiss(ip,appId,id);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/invite_call",method = RequestMethod.POST)
    public ApiGatewayResponse invite(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                @RequestHeader(value = "AppID") String appId,
                                @RequestBody ConfInviteCallInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("邀请会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = confService.invite(ip,appId,id,
                dto.getFrom(),dto.getTo(),dto.getMaxDuration(),dto.getMaxDialDuration(),
                dto.getDialVoiceStopCond(),dto.getPlayFile(),dto.getVoiceMode());
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/join",method = RequestMethod.POST)
    public ApiGatewayResponse join(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                   @RequestHeader(value = "AppID") String appId,
                                   @RequestBody ConfJoinInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("加入会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.join(ip,appId,id,dto.getCallId(),dto.getMaxDuration(),dto.getPlayFile(),dto.getVoiceMode());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/quit",method = RequestMethod.POST)
    public ApiGatewayResponse quit(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID") String appId,
                             @RequestBody ConfQuitInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("推出会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.quit(ip,appId,id,dto.getCallId());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/start_play",method = RequestMethod.POST)
    public ApiGatewayResponse play(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID") String appId,
                             @RequestBody ConfPlayInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("会议放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.startPlay(ip,appId,id,dto.getFiles());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/stop_play",method = RequestMethod.POST)
    public ApiGatewayResponse stopPlay(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("会议停止放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean result = confService.stopPlay(ip,appId,id);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/start_record",method = RequestMethod.POST)
    public ApiGatewayResponse record(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID") String appId,
                             @RequestBody ConfRecordInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("会议放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.startRecord(ip,appId,id,dto.getMaxDuration());
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/stop_record",method = RequestMethod.POST)
    public ApiGatewayResponse stopRecord(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID") String appId) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("会议停止放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.stopRecord(ip,appId,id);
        return ApiGatewayResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/set_voice_mode",method = RequestMethod.POST)
    public ApiGatewayResponse setVoiceMode(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                   @RequestHeader(value = "AppID") String appId,
                                   @RequestBody ConfSetVoiceModeInputDTO dto) throws YunhuniApiException {
        if(logger.isDebugEnabled()){
            logger.debug("设置会议成员录放音模式API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,dto);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = confService.setVoiceMode(ip,appId,id,dto.getCallId(),dto.getVoiceMode());
        return ApiGatewayResponse.success(result);
    }
}
