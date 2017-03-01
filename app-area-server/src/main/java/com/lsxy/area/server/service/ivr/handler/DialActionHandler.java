package com.lsxy.area.server.service.ivr.handler;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.AreaAndTelNumSelector;
import com.lsxy.area.server.service.callcenter.CallCenterUtil;
import com.lsxy.area.server.service.callcenter.ConversationService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.CallbackUrlUtil;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.exceptions.api.YunhuniApiException;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.LineGatewayService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
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

    @Autowired
    private ConversationService conversationService;

    @Reference(lazy = true,check = false,timeout = 3000)
    private CallCenterService callCenterService;

    @Autowired
    private CallbackUrlUtil callbackUrlUtil;

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
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        //更新下一步
        businessStateService.updateInnerField(callId,IVRActionService.IVR_NEXT_FIELD,next);
        dial(state.getSubaccountId(),callId,state.getResId(),state.getAppId(),state.getTenantId(),conversationService.isCC(state),state.getBusinessData().get(CallCenterUtil.CALLCENTER_FIELD),root);
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

    public boolean dial(String subaccountId,String ivr_call_id, String parent_call_res_id, String appId, String tenantId,boolean isCC,String callCenterId, Element root){
        App app = appService.findById(appId);
        //解析xml
        String ring_play_file = root.elementTextTrim("play");
        String to = root.elementTextTrim("number");
        Integer maxCallDuration=parseInt(root.attributeValue("max_call_duration"));
        Integer maxDialDuration=parseInt(root.attributeValue("max_dial_duration"));
        Integer dialVoiceStopCond = parseInt(root.attributeValue("dial_voice_stop_cond"));
        String from = root.attributeValue("from");
        String max_seconds=null;
        String connect_mode = null;
        String recording = null;
        String volume1 = null;
        String volume2 = null;
        String play_time = null;
        String play_file=null;
        String play_repeat= null;
        Element connectEle = root.element("connect");
        if(connectEle!=null){
            max_seconds = connectEle.attributeValue("max_duration");
            connect_mode = connectEle.attributeValue("mode");
            recording = connectEle.attributeValue("recording");
            volume1 = connectEle.attributeValue("volume1");
            volume2 = connectEle.attributeValue("volume2");
            play_time = connectEle.attributeValue("play_time");
            Element playEle = connectEle.element("play");
            if(playEle != null){
                play_file = playEle.getTextTrim();
                play_repeat = playEle.attributeValue("repeat");
            }
        }

        try{
            ring_play_file = playFileUtil.convert(tenantId,appId,ring_play_file);
        }catch (Throwable t){
            logger.error("",t);
        }

        AreaAndTelNumSelector.Selector selector;
        try {
            selector = areaAndTelNumSelector.getTelnumberAndAreaId(subaccountId,app,from,to);
        } catch (YunhuniApiException e) {
            return false;
        }
        String areaId = selector.getAreaId();
        String oneTelnumber = selector.getOneTelnumber();
        String lineId = selector.getLineId();

        String callId = null;

        CallSession callSession = null;

        if(isCC){
            callId = UUIDGenerator.uuid();

            callSession = new CallSession();
            callSession.setStatus(CallSession.STATUS_CALLING);
            callSession.setFromNum(to);
            callSession.setToNum(from);
            callSession.setAppId(app.getId());
            callSession.setTenantId(app.getTenant().getId());
            callSession.setRelevanceId(callId);
            callSession.setResId(null);
            callSession.setType(CallSession.TYPE_CALL_CENTER);
            callSession = callSessionService.save(callSession);
        }else{
            VoiceIvr voiceIvr = new VoiceIvr();
            voiceIvr.setAppId(app.getId());
            voiceIvr.setTenantId(app.getTenant().getId());
            voiceIvr.setFromNum(oneTelnumber);
            voiceIvr.setToNum(to);
            voiceIvr.setStartTime(new Date());
            voiceIvr.setIvrType(VoiceIvr.IVR_TYPE_CALL);
            voiceIvr = voiceIvrService.save(voiceIvr);
            callId = voiceIvr.getId();

            callSession = new CallSession();
            callSession.setStatus(CallSession.STATUS_PREPARING);
            callSession.setFromNum(oneTelnumber);
            callSession.setToNum(selector.getToUri());
            callSession.setAppId(app.getId());
            callSession.setTenantId(app.getTenant().getId());
            callSession.setRelevanceId(callId);
            callSession.setType(CallSession.TYPE_VOICE_IVR);
            callSession.setResId(null);
            callSession = callSessionService.save(callSession);
        }

        Map<String, Object> params = new MapBuilder<String,Object>()
                .putIfNotEmpty("to_uri",selector.getToUri())
                .putIfNotEmpty("from_uri",oneTelnumber)
                .putIfNotEmpty("parent_call_res_id",parent_call_res_id)
                .putIfNotEmpty("ring_play_file",ring_play_file)
                .put("max_answer_seconds",maxCallDuration, IVRActionService.MAX_DURATION_SEC)
                .putIfNotEmpty("max_ring_seconds",maxDialDuration)
                .putIfNotEmpty("user_data",callId)
                .put("areaId",areaId)
                .putIfNotEmpty(BusinessState.REF_RES_ID,parent_call_res_id)
                .build();

        RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL, params);
        try {
            if(!businessStateService.closed(callId)){
                rpcCaller.invoke(sessionContext, rpcrequest);
            }
        } catch (Exception e) {
            logger.error("ivr 拨号出错:",e);
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","ivr.connect_end")
                    .putIfNotEmpty("id",ivr_call_id)
                    .putIfNotEmpty("subaccount_id",subaccountId)
                    .putIfNotEmpty("begin_time",System.currentTimeMillis())
                    .putIfNotEmpty("end_time",System.currentTimeMillis())
                    .putIfNotEmpty("error","dial error")
                    .build();
            notifyCallbackUtil.postNotify(app.getUrl(),notify_data,null,3);
            ivrActionService.doAction(ivr_call_id,new MapBuilder<String,Object>()
                    .putIfNotEmpty("error","dial error")
                    .build());
            return false;
        }

        //保存业务数据，后续事件要用到
        BusinessState callstate = new BusinessState.Builder()
                .setTenantId(tenantId)
                .setAppId(appId)
                .setSubaccountId(subaccountId)
                .setId(callId)
                .setType(BusinessState.TYPE_IVR_DIAL)
                .setCallBackUrl(callbackUrlUtil.get(app,subaccountId))
                .setAreaId(areaId)
                .setLineGatewayId(lineId)
                .setBusinessData(new MapBuilder<String,String>()
                        .putIfNotEmpty(BusinessState.REF_RES_ID,parent_call_res_id)
                        .putIfWhere(CallCenterUtil.ISCC_FIELD,isCC,CallCenterUtil.ISCC_TRUE)
                        .putIfWhere(CallCenterUtil.CALLCENTER_FIELD,isCC,callCenterId)
                        .putIfNotEmpty("ivr_call_id",ivr_call_id)
                        .putIfNotEmpty("from",from)
                        .putIfNotEmpty("to",to)
                        .put("max_seconds",max_seconds, ""+IVRActionService.MAX_DURATION_SEC)
                        .put("connect_mode",connect_mode,"1")
                        .putIfNotEmpty("volume1",volume1)
                        .putIfNotEmpty("volume2",volume2)
                        .putIfNotEmpty("play_time",play_time)
                        .putIfNotEmpty("play_file",play_file)
                        .put("play_repeat",play_repeat,"1")
                        .putIfNotEmpty("recording",recording)
                        .putIfNotEmpty(BusinessState.SESSIONID,callSession.getId())
                        .build())
                .build();
        businessStateService.save(callstate);
        return true;
    }
}
