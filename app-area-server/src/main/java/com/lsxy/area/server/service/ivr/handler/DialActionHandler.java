package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.api.exceptions.AppOffLineException;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * dail动作指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
public class DialActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private AppService appService;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private LineGatewayService lineGatewayService;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Autowired
    private CallSessionService callSessionService;

    @Autowired
    private AreaAndTelNumSelector areaAndTelNumSelector;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Override
    public String getAction() {
        return "dial";
    }

    /**
     * 1向区域代理发起呼叫
     * 2在拨号结束事件中向区域代理发送双通道连接命令
     * 3监听双通道调用成功事件，向开发者发送通知
     * 4监听双通道结束事件 向开发者发送通知
     * 5监听被叫方呼叫结束事件（此处要继续调用doAction）
     * @param callId
     * @param root
     * @return
     */
    @Override
    public boolean handle(String callId, Element root,String next) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},ivr={}",callId,getAction());
        }
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作",getAction());
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }
        try{
            //更新下一步
            Map<String,Object> businessData = state.getBusinessData();
            if(businessData == null){
                businessData = new HashMap<>();
            }
            businessData.put("next",next);
            state.setBusinessData(businessData);
            businessStateService.save(state);
            dial(callId,state.getResId(),state.getAppId(),state.getTenantId(),root);
        }catch (Throwable t){
            logger.error("ivr拨号失败:",t);
            App app = appService.findById(state.getAppId());
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","ivr.connect_end")
                    .putIfNotEmpty("id",callId)
                    .putIfNotEmpty("begin_time",System.currentTimeMillis())
                    .putIfNotEmpty("end_time",System.currentTimeMillis())
                    .putIfNotEmpty("error","dial error")
                    .build();
            if(notifyCallbackUtil.postNotifySync(app.getUrl(),notify_data,null,3)){
                ivrActionService.doAction(callId);
            }
        }
        return true;
    }

    private Integer parseInt(String s){
        if(StringUtils.isBlank(s)){
            return null;
        }
        return Integer.parseInt(s);
    }

    private Long parseLong(String s){
        if(StringUtils.isBlank(s)){
            return null;
        }
        return Long.parseLong(s);
    }

    public boolean dial(String ivr_call_id,String parent_call_res_id,String appId,String tenantId, Element root){
        App app = appService.findById(appId);


        //解析xml
        String ring_play_file = root.elementTextTrim("play");
        String to = root.elementTextTrim("number");
        Integer maxCallDuration=parseInt(root.attributeValue("max_call_duration"));
        Integer maxDialDuration=parseInt(root.attributeValue("max_dial_duration"));
        Integer dialVoiceStopCond = parseInt(root.attributeValue("dial_voice_stop_cond"));
        String from = root.attributeValue("from");
        Integer max_seconds=null;
        Integer connect_mode = null;
        boolean recording = false;
        Integer volume1 = null;
        Integer volume2 = null;
        Long play_time = null;
        String play_file=null;
        Integer play_repeat= null;
        Element connectEle = root.element("connect");
        if(connectEle!=null){
            max_seconds = parseInt(connectEle.attributeValue("max_duration"));
            connect_mode = parseInt(connectEle.attributeValue("mode"));
            recording = Boolean.parseBoolean(connectEle.attributeValue("recording"));
            volume1 = parseInt(connectEle.attributeValue("volume1"));
            volume2 = parseInt(connectEle.attributeValue("volume2"));
            play_time = parseLong(connectEle.attributeValue("play_time"));
            Element playEle = connectEle.element("play");
            if(playEle != null){
                play_file = playEle.getTextTrim();
                play_repeat = parseInt(playEle.attributeValue("repeat"));
            }
        }

        try{
            ring_play_file = playFileUtil.convert(tenantId,appId,ring_play_file);
        }catch (Throwable t){
            logger.error("",t);
        }

        AreaAndTelNumSelector.Selector selector;
        try {
            selector = areaAndTelNumSelector.getTelnumberAndAreaId(app,from,to);
        } catch (AppOffLineException e) {
            return false;
        }
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber().getTelNumber();
        LineGateway lineGateway = lineGatewayService.getBestLineGatewayByNumber(oneTelnumber);


        VoiceIvr voiceIvr = new VoiceIvr();
        voiceIvr.setFromNum(oneTelnumber);
        voiceIvr.setToNum(to);
        voiceIvr.setStartTime(new Date());
        voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_CALL);
        voiceIvr = voiceIvrService.save(voiceIvr);
        String callId = voiceIvr.getId();

        CallSession callSession = new CallSession();
        callSession.setStatus(CallSession.STATUS_PREPARING);
        callSession.setFromNum(oneTelnumber);
        callSession.setToNum(to+"@"+lineGateway.getSipProviderIp());
        callSession.setApp(app);
        callSession.setTenant(app.getTenant());
        callSession.setRelevanceId(callId);
        callSession.setType(CallSession.TYPE_VOICE_IVR);
        callSession.setResId(null);
        callSession = callSessionService.save(callSession);

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",to+"@"+lineGateway.getSipProviderIp())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("parent_call_res_id",parent_call_res_id)
                .putIfNotEmpty("ring_play_file",ring_play_file)
                .put("max_answer_seconds",maxCallDuration, IVRActionService.MAX_DURATION_SEC)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .put("areaId ",areaId)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            rpcCaller.invoke(sessionContext, rpcrequest);
        } catch (Exception e) {
            logger.error("ivr 拨号出错:",e);
        }

        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState.Builder()
                .setTenantId(tenantId)
                .setAppId(appId)
                .setId(callId)
                .setType("ivr_dial")
                .setAreaId(areaId)
                .setLineGatewayId(lineGateway.getId())
                .setBusinessData(new MapBuilder<String,Object>()
                        .putIfNotEmpty("ivr_call_id",ivr_call_id)
                        .putIfNotEmpty("from",from)
                        .putIfNotEmpty("to",to)
                        .put("max_seconds",max_seconds, IVRActionService.MAX_DURATION_SEC)
                        .put("connect_mode",connect_mode,1)
                        .putIfNotEmpty("volume1",volume1)
                        .putIfNotEmpty("volume2",volume2)
                        .putIfNotEmpty("play_time",play_time)
                        .putIfNotEmpty("play_file",play_file)
                        .put("play_repeat",play_repeat,1)
                        .putIfNotEmpty("recording",recording)
                        .putIfNotEmpty("sessionid",callSession.getId())
                        .build())
                .build();
        businessStateService.save(callstate);
        return true;
    }
}
