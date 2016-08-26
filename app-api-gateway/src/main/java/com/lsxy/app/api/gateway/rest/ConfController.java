package com.lsxy.app.api.gateway.rest;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.lsxy.app.api.gateway.StasticsCounter;
import com.lsxy.app.api.gateway.dto.*;
import com.lsxy.area.api.ConfService;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.web.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired(required = false)
    private StasticsCounter sc;

    @Reference(timeout=3000)
    private ConfService confService;

    @RequestMapping(value = "/{accountId}/conf/create",method = RequestMethod.POST)
    public RestResponse create(HttpServletRequest request, @PathVariable String accountId,
                                    @RequestHeader(value = "AppID",required = false) String appId,
                                    @RequestBody ConfCreateInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("创建会议API参数,accountId={},appId={},dto={}",accountId,appId,JSON.toJSON(dto));
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = null;
        try {
            callId = confService.create(ip,appId,dto.getMaxDuration(),dto.getMaxParts(),
                    dto.getRecording(),dto.getAutoHangup(),dto.getBgmFile(),dto.getCallbackUrl(),dto.getUserData());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return RestResponse.success(result);
    }


    @RequestMapping(value = "/{accountId}/conf/{id}/dismiss",method = RequestMethod.POST)
    public RestResponse dismiss(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                               @RequestHeader(value = "AppID",required = false) String appId) {
        if(logger.isDebugEnabled()){
            logger.debug("解散会议API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean result = false;
        try {
            result = confService.dismiss(ip,appId,id);
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/invite_call",method = RequestMethod.POST)
    public RestResponse invite(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                @RequestHeader(value = "AppID",required = false) String appId,
                                @RequestBody ConfInviteCallInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("邀请会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,JSON.toJSON(dto));
        }
        String ip = WebUtils.getRemoteAddress(request);
        String callId = null;
        try {
            callId = confService.invite(ip,appId,id,
                    dto.getFrom(),dto.getTo(),dto.getCustomFrom(),dto.getCustomTo(),
                    dto.getMaxDuration(),dto.getMaxDialDuration(),
                    dto.getDialVoiceStopCond(),dto.getPlayFile(),dto.getVoiceMode());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        Map<String,String> result = new HashMap<>();
        result.put("callId",callId);
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/join",method = RequestMethod.POST)
    public RestResponse join(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                   @RequestHeader(value = "AppID",required = false) String appId,
                                   @RequestBody ConfJoinInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("加入会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,JSON.toJSON(dto));
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.join(ip,appId,id,dto.getCallId(),dto.getMaxDuration(),dto.getPlayFile(),dto.getVoiceMode());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/quit",method = RequestMethod.POST)
    public RestResponse quit(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID",required = false) String appId,
                             @RequestBody ConfQuitInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("推出会议API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,JSON.toJSON(dto));
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.quit(ip,appId,id,dto.getCallId());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/start_play",method = RequestMethod.POST)
    public RestResponse play(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID",required = false) String appId,
                             @RequestBody ConfPlayInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("会议放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.startPlay(ip,appId,id,dto.getFiles());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/stop_play",method = RequestMethod.POST)
    public RestResponse stopPlay(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID",required = false) String appId) {
        if(logger.isDebugEnabled()){
            logger.debug("会议停止放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.stopPlay(ip,appId,id);
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/start_record",method = RequestMethod.POST)
    public RestResponse record(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID",required = false) String appId,
                             @RequestBody ConfRecordInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("会议放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.startRecord(ip,appId,id,dto.getMaxDuration());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/stop_record",method = RequestMethod.POST)
    public RestResponse stopRecord(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                             @RequestHeader(value = "AppID",required = false) String appId) {
        if(logger.isDebugEnabled()){
            logger.debug("会议停止放音API参数,accountId={},appId={},confId={}",accountId,appId,id);
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.stopRecord(ip,appId,id);
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }

    @RequestMapping(value = "/{accountId}/conf/{id}/set_voice_mode",method = RequestMethod.POST)
    public RestResponse setVoiceMode(HttpServletRequest request, @PathVariable String accountId,@PathVariable String id,
                                   @RequestHeader(value = "AppID",required = false) String appId,
                                   @RequestBody ConfSetVoiceModeInputDTO dto) {
        if(logger.isDebugEnabled()){
            logger.debug("设置会议成员录放音模式API参数,accountId={},appId={},confId={},dto={}",accountId,appId,id,JSON.toJSON(dto));
        }
        String ip = WebUtils.getRemoteAddress(request);
        boolean  result = false;
        try {
            result = confService.setVoiceMode(ip,appId,id,dto.getCallId(),dto.getVoiceMode());
        } catch (Exception e) {
            return RestResponse.failed("0000x",e.getMessage());
        }
        return RestResponse.success(result);
    }
}
