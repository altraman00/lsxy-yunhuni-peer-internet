package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.area.server.util.PlayFileUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.session.SessionContext;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * play指令处理器
 * Created by liuws on 2016/9/2.
 */
@Component
public class PlayActionHandler extends ActionHandler{

    @Autowired
    private BusinessStateService businessStateService;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private SessionContext sessionContext;

    @Autowired
    private PlayFileUtil playFileUtil;

    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;

    @Autowired
    private IVRActionService ivrActionService;

    @Override
    public String getAction() {
        return "play";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String finish_keys = root.attributeValue("finish_keys");
        String repeat = root.attributeValue("repeat");
        List<String> plays = new ArrayList<String>();
        if(StringUtils.isNotBlank(root.getTextTrim())){
            plays.add(root.getTextTrim());
        }
        String res_id = state.getResId();
        if(plays!=null && plays.size()>0){
            businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
            try {
                plays = playFileUtil.convertArray(state.getTenantId(),state.getAppId(),plays);
                Map<String, Object> params = new MapBuilder<String,Object>()
                        .putIfNotEmpty("res_id",res_id)
                        .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{StringUtils.join(plays,"|"),7,""}}))
                        .putIfNotEmpty("finish_keys",finish_keys)
                        .putIfNotEmpty("repeat",repeat)
                        .putIfNotEmpty("user_data",callId)
                        .put("areaId",state.getAreaId())
                        .build();

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_START, params);
                if(!businessStateService.closed(callId)) {
                    rpcCaller.invoke(sessionContext, rpcrequest);
                }
            } catch (Throwable e) {
                logger.error("调用失败",e);
                if(StringUtils.isNotBlank(state.getCallBackUrl())){
                    Map<String, Object> notify_data = new MapBuilder<String, Object>()
                            .putIfNotEmpty("event", "ivr.play_end")
                            .putIfNotEmpty("id", callId)
                            .putIfNotEmpty("subaccount_id",state.getSubaccountId())
                            .putIfNotEmpty("begin_time", System.currentTimeMillis())
                            .putIfNotEmpty("end_time", System.currentTimeMillis())
                            .putIfNotEmpty("error", "play error")
                            .build();
                    notifyCallbackUtil.postNotify(state.getCallBackUrl(),notify_data,null,3);
                }
                ivrActionService.doAction(callId,new MapBuilder<String,Object>()
                        .putIfNotEmpty("error","play error")
                        .build());
            }
        }
        return true;
    }
}
