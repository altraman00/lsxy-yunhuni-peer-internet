package com.lsxy.area.server.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.CallService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.StasticsCounter;
import com.lsxy.area.server.test.TestIncomingZB;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.area.server.util.RecordFileUtil;
import com.lsxy.area.server.util.SipUrlUtil;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.core.exceptions.api.*;
import com.lsxy.framework.core.utils.*;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.config.service.ApiGwRedBlankNumService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.CaptchaCall;
import com.lsxy.yunhuni.api.session.model.NotifyCall;
import com.lsxy.yunhuni.api.session.model.VoiceCallback;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.CaptchaCallService;
import com.lsxy.yunhuni.api.session.service.NotifyCallService;
import com.lsxy.yunhuni.api.session.service.VoiceCallbackService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/18.
 */
@Service
@Component
public class CallServiceImpl implements CallService {

    private static final Logger logger = LoggerFactory.getLogger(CallServiceImpl.class);

    @Autowired(required = false)
    private StasticsCounter cs;

    @Autowired(required = false)
    private TestIncomingZB tzb;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private ApiGwRedBlankNumService apiGwRedBlankNumService;

    @Autowired
    private AppService appService;

    @Autowired
    private CalCostService calCostService;

    @Autowired
    BusinessStateService businessStateService;

    @Autowired
    LineGatewayService lineGatewayService;

    @Autowired
    VoiceCallbackService voiceCallbackService;

    @Autowired
    NotifyCallService notifyCallService;

    @Autowired
    CallSessionService callSessionService;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

    @Autowired
    private CaptchaCallService captchaCallService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

    @Override
    public String call(String subaccountId,String from, String to, int maxAnswerSec, int maxRingSec) throws YunhuniApiException {

        String callid = UUIDGenerator.uuid();
        String params = "to=%s&from=%s&maxAnswerSec=%d&maxRingSec=%d&callid=%s";
        params = String.format(params, to, from, maxAnswerSec, maxRingSec, callid);

        try {
            //找到合适的区域代理

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
                try {
                    if (logger.isDebugEnabled()) {
                        logger.debug("发送SYS_CALL指令到区域:{}", rpcrequest);
                    }

                    /*发送给区域的请求次数计数*/
                    if (cs != null) cs.getSendAreaNodeRequestCount().incrementAndGet();

                    tzb.doCallZB(to,rpcrequest);

                    rpcCaller.invoke(sessionContext, rpcrequest);

                    /*呼叫API调用次数计数*/
                    if(cs!=null)cs.getSendAreaNodeSysCallCount().incrementAndGet();
                } catch (Exception e) {
                    throw new InvokeCallException(e);
                }
            return callid;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String duoCallback(String subaccountId,String ip,String appId,String from1,String to1,String from2,String to2,String ring_tone,Integer ring_tone_mode,
                              Integer max_dial_duration,Integer max_call_duration ,Boolean recording,Integer record_mode,String user_data) throws YunhuniApiException {
        String apiCmd = BusinessState.TYPE_DUO_CALL;
        String duocCallId;
        if(apiGwRedBlankNumService.isRedNum(to1) || apiGwRedBlankNumService.isRedNum(to2)){
            throw new NumberNotAllowToCallException(
                    new ExceptionContext().put("to1",to1).put("to2",to2)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("appId",appId)
                        .put("ip",ip)
                );
            }
        }
        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.VoiceCallback)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("appId",appId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,apiCmd, app.getTenant().getId());

        //TODO 获取号码
        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(subaccountId,app,true, from1,to1,from2, to2);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();

        from1 = oneTelnumber;
        from2 = oneTelnumber;
        //TODO 获取线路IP和端口
        String lineId = selector.getLineId();
        String to1_uri = selector.getTo1Uri();
        String to2_uri = selector.getTo2Uri();

        //保存双向回拔表
        VoiceCallback voiceCallback = new VoiceCallback(from1,from2,to1_uri,to2_uri);
        voiceCallback.setAppId(app.getId());
        voiceCallback.setTenantId(app.getTenant().getId());
        voiceCallbackService.save(voiceCallback);
        duocCallId = voiceCallback.getId();
        CallSession callSession = new CallSession(CallSession.STATUS_PREPARING,app.getId(),app.getTenant().getId(),duocCallId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to1_uri);
        CallSession callSession2 = new CallSession(CallSession.STATUS_PREPARING,app.getId(),app.getTenant().getId(),duocCallId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to2_uri);
        callSessionService.save(callSession);
        callSessionService.save(callSession2);

        Map<String, Object> params = new MapBuilder<String, Object>()
                .putIfNotEmpty("from1_uri", from1)
                .putIfNotEmpty("to1_uri",to1_uri)
                .putIfNotEmpty("from2_uri", from2)
                .putIfNotEmpty("to2_uri",to2_uri)
                .putIfNotEmpty("max_connect_seconds",max_call_duration)
                .putIfNotEmpty("max_ring_seconds",max_dial_duration)
                .putIfNotEmpty("user_data1",duocCallId)
                .putIfNotEmpty("user_data2",duocCallId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        if(StringUtils.isNotBlank(ring_tone)){
            ring_tone = playFileUtil.convertArray(app.getTenant().getId(),appId,ring_tone);
            params.put("ring_play_file",ring_tone);
            params.put("ring_play_mode",ring_tone_mode);
        }
        //录音
        if(recording != null && recording){
            params.put("record_file ", RecordFileUtil.getRecordFileUrl(app.getTenant().getId(),appId));
            params.put("record_mode",record_mode);
            params.put("record_format ",1);
        }

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK, params);
        try {
            Map<String,String> data = new MapBuilder<String,String>()
                    .put(to1_uri.split("@")[0],callSession.getId())
                    .put(to2_uri.split("@")[0],callSession2.getId())
                    .build();
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                                    .setTenantId(app.getTenant().getId())
                                    .setAppId(appId)
                                    .setSubaccountId(subaccountId)
                                    .setId(duocCallId)
                                    .setType(apiCmd)
                                    .setUserdata(user_data)
                                    .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                                    .setAreaId(areaId)
                                    .setLineGatewayId(lineId)
                                    .setBusinessData(data)
                                    .build();

            businessStateService.save(cache);

            rpcCaller.invoke(sessionContext, rpcrequest);

            return duocCallId;
        }catch(Exception e){
            logger.error("消息发送到区域失败:{}", rpcrequest,e);
            throw new InvokeCallException(e);
        }
    }

    @Override
    public void duoCallbackCancel(String subaccountId,String ip, String appId, String callId) throws YunhuniApiException{
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("appId",appId)
                        .put("ip",ip)
                );
            }
        }
        BusinessState state = businessStateService.get(callId);

        if(state == null){
            throw new CallNotExistsException(
                    new ExceptionContext().put("appId",appId)
                            .put("callId",callId)
            );
        }

        if(state.getResId() == null){
            throw new SystemBusyException(
                    new ExceptionContext().put("appId",appId)
                            .put("callId",callId)
            );
        }

        if(state.getClosed()!= null && state.getClosed()){
            throw new CallNotExistsException();
        }

        String areaId = areaAndTelNumSelector.getAreaId(app);
        Map<String, Object> params = new MapBuilder<String, Object>()
                .put("res_id",state.getResId())
                .put("user_data ",state.getId())
                .put("areaId",areaId)
                .build();
        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_DUO_CALLBACK_CANCEL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        }catch(Exception e){
            logger.error("消息发送到区域失败:{}", rpcrequest,e);
            throw new InvokeCallException(e);
        }

    }

    @Override
    public String notifyCall(String subaccountId,String ip, String appId, String from,String to,String play_file,List<List<Object>> play_content,
                             Integer repeat,Integer max_dial_duration,String user_data) throws YunhuniApiException{
        String apiCmd = BusinessState.TYPE_NOTIFY_CALL;
        String callId;
        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("to",to)
                                .put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }

        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.VoiceDirectly)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,apiCmd, app.getTenant().getId());

        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(subaccountId,app, from,to);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();

        from = oneTelnumber;
        //TODO 获取线路IP和端口
        String lineId = selector.getLineId();
        String to_uri = selector.getToUri();
        //TODO 获取线路IP和端口

        //保存语音通知
        NotifyCall notifyCall = new NotifyCall(from,to_uri);
        notifyCall.setAppId(app.getId());
        notifyCall.setTenantId(app.getTenant().getId());
        notifyCallService.save(notifyCall);
        callId = notifyCall.getId();
        CallSession callSession = new CallSession(CallSession.STATUS_PREPARING,app.getId(),app.getTenant().getId(),callId, ProductCode.changeApiCmdToProductCode(apiCmd).name(),oneTelnumber,to_uri);
        callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String, Object>()
                .putIfNotEmpty("from_uri", from)
                .putIfNotEmpty("from_uri", from)
                .putIfNotEmpty("to_uri", to_uri)
                .putIfNotEmpty("play_repeat",repeat)
                .putIfNotEmpty("max_ring_seconds",max_dial_duration)
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("play_content",this.getPlayContent(app.getTenant().getId(),appId,play_file,play_content))
                .putIfNotEmpty("areaId",areaId)
                .build();

        try {
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_NOTIFY_CALL, params);
            Map<String,String> data = new MapBuilder<String,String>()
                    .put(BusinessState.SESSIONID,callSession.getId())
                    .build();
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                                    .setTenantId(app.getTenant().getId())
                                    .setAppId(app.getId())
                                    .setSubaccountId(subaccountId)
                                    .setId(callId)
                                    .setType(apiCmd)
                                    .setUserdata(user_data)
                                    .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                                    .setAreaId(areaId)
                                    .setLineGatewayId(lineId)
                                    .setBusinessData(data)
                                    .build();
            businessStateService.save(cache);


            rpcCaller.invoke(sessionContext, rpcrequest);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    @Override
    public String verifyCall(String subaccountId,String ip, String appId, String from, String to, Integer maxDialDuration, String verifyCode, String playFile, Integer repeat, String userData) throws YunhuniApiException {

        if(apiGwRedBlankNumService.isRedNum(to)){
            throw new NumberNotAllowToCallException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        App app = appService.findById(appId);
        if(app == null){
            throw new AppNotFoundException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }

        String whiteList = app.getWhiteList();
        if(StringUtils.isNotBlank(whiteList)){
            if(!whiteList.contains(ip)){
                throw new IPNotInWhiteListException(
                        new ExceptionContext().put("to",to)
                                .put("subaccountId",subaccountId)
                                .put("appId",appId)
                                .put("ip",ip)
                );
            }
        }

        if(!appService.enabledService(app.getTenant().getId(),appId, ServiceType.VoiceValidate)){
            throw new AppServiceInvalidException(
                    new ExceptionContext().put("to",to)
                            .put("subaccountId",subaccountId)
                            .put("appId",appId)
            );
        }
        //判断余额配额是否充足
        calCostService.isCallTimeRemainOrBalanceEnough(subaccountId,ProductCode.captcha_call.getApiCmd(), app.getTenant().getId());

        AreaAndTelNumSelector.Selector selector = areaAndTelNumSelector.getTelnumberAndAreaId(subaccountId,app, from,to);
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();
        String lineId = selector.getLineId();

        CaptchaCall captchaCall = new CaptchaCall();
        captchaCall.setAppId(app.getId());
        captchaCall.setTenantId(app.getTenant().getId());
        captchaCall.setStartTime(new Date());
        captchaCall.setEndTime(null);
        captchaCall.setFromNum(oneTelnumber);
        captchaCall.setToNum(to);
        captchaCall.setHangupSide(null);
        captchaCall.setResId(null);
        captchaCall = captchaCallService.save(captchaCall);
        String callId = captchaCall.getId();

        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(selector.getToUri());
        callSession.setAppId(app.getId());
        callSession.setTenantId(app.getTenant().getId());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_VOICE_VOICECODE);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String, Object>()
                .putIfNotEmpty("to_uri",selector.getToUri())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("play_repeat",repeat)
                .putIfNotEmpty("user_data",callId)
                .putIfNotEmpty("areaId",areaId)
                .build();

        playFile = playFileUtil.convertArray(app.getTenant().getId(),appId,playFile);

        if(StringUtils.isNotBlank(playFile) && StringUtils.isNotBlank(verifyCode)){
            Object[][] plays = new Object[][]{new Object[]{playFile,0,""},new Object[]{verifyCode,1,""}};
            params.put("play_content", JSONUtil2.objectToJson(plays));
        }else if(StringUtils.isNotBlank(playFile)){
            Object[][] plays = new Object[][]{new Object[]{playFile,0,""}};
            params.put("play_content", JSONUtil2.objectToJson(plays));
        }else if(StringUtils.isNotBlank(verifyCode)){
            Object[][] plays = new Object[][]{new Object[]{verifyCode,1,""}};
            params.put("play_content", JSONUtil2.objectToJson(plays));
        }

        try {
            //找到合适的区域代理
            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_EXT_VERIFY_CALL, params);
            rpcCaller.invoke(sessionContext, rpcrequest);
            //将数据存到redis
            BusinessState cache = new BusinessState.Builder()
                    .setTenantId(app.getTenant().getId())
                    .setAppId(app.getId())
                    .setSubaccountId(subaccountId)
                    .setId(callId)
                    .setType(BusinessState.TYPE_VERIFY_CALL)
                    .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                    .setUserdata(userData)
                    .setAreaId(areaId)
                    .setLineGatewayId(lineId)
                    .setBusinessData(new MapBuilder<String,String>()
                            .putIfNotEmpty("from",SipUrlUtil.extractTelnum(oneTelnumber))
                            .putIfNotEmpty("to", SipUrlUtil.extractTelnum(to))
                            .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                            .build())
                    .build();
            businessStateService.save(cache);
            return callId;
        }catch(Exception ex){
            throw new InvokeCallException(ex);
        }
    }

    /**
     * 转换成cti接口要的二维数组字符串
     * @param tenantId
     * @param appId
     * @param play_file 播放文件列表
     * @param dtos 播放文件内容
     * @return
     */
    public String getPlayContent(String tenantId,String appId,String play_file,List<List<Object>> dtos) throws YunhuniApiException {
        if(dtos == null){
            dtos = new ArrayList<>();
        }
        if(StringUtils.isNotBlank(play_file)){
            List<Object> playFile = new ArrayList<>();
            playFile.add(play_file);
            playFile.add(7);
            playFile.add("");
            dtos.add(0,playFile);
        }
        if(dtos != null){
            for(List<Object> play:dtos){
                Integer type;
                try{
                    type = Integer.valueOf(play.get(1) + "");
                }catch (Exception e){
                    throw new RequestIllegalArgumentException();
                }
                if(type == null || type < 0 || (type > 5 && type != 7)){
                    throw new RequestIllegalArgumentException();
                }
                String content = play.get(0) + "";
                switch(type){
                    case 0:
                        //文件播放。此时，放音内容应是文件名字符串。
                        play.set(0,playFileUtil.convertArray(tenantId, appId, content));
                        break;
                    case 1:
                        //数字播放。此时，放音内容应是十进制整数。
                        try{
                            Integer.valueOf(content);
                        }catch (Exception e){
                            throw new RequestIllegalArgumentException();
                        }
                        break;
                    case 2:
                        //数值播放。此时，放音内容应是十进制整数或者浮点数。
                        try{
                            Double.valueOf(content);
                        }catch (Exception e){
                            throw new RequestIllegalArgumentException();
                        }
                        break;
                    case 3:
                        //金额播放。此时，放音内容应是十进制整数或者浮点数。
                        try{
                            Double.valueOf(content);
                        }catch (Exception e){
                            throw new RequestIllegalArgumentException();
                        }
                        break;
                    case 4:
                        //日期时间播放。
                        try {
                            org.apache.commons.lang3.time.DateUtils.parseDate(content,"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd HH:mm","yyyy-MM-dd","yyyy-MM","HH:mm:ss","HH:mm");
                        } catch (ParseException e) {
                            throw new RequestIllegalArgumentException();
                        }
                        break;
                    case 5:
                        //时长播放。
                        try {
                            org.apache.commons.lang3.time.DateUtils.parseDate(content,"HH:mm:ss","HH:mm");
                        } catch (ParseException e) {
                            throw new RequestIllegalArgumentException();
                        }
                        break;
                    case 7:
                        //文件播放。此时，放音内容应是文件名字符串。
                        play.set(0,playFileUtil.convertArray(tenantId, appId, content));
                        break;
                }
            }
        }
        return JSONUtil.objectToJson(dtos);
    }

}
