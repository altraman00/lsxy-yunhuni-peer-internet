package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
import com.lsxy.area.server.service.ivr.IVRActionService;
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

    @Override
    public String getAction() {
        return "play";
    }

    @Override
    public boolean handle(String callId,BusinessState state, Element root,String next) {
        String finish_keys = root.attributeValue("finish_keys");
        List<String> plays = new ArrayList<String>();
        if(StringUtils.isNotBlank(root.getTextTrim())){
            plays.add(root.getTextTrim());
        }
        String res_id = state.getResId();
        if(plays!=null && plays.size()>0){
            try {
                plays = playFileUtil.convertArray(state.getTenantId(),state.getAppId(),plays);
                Map<String, Object> params = new MapBuilder<String,Object>()
                        .putIfNotEmpty("res_id",res_id)
                        .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{StringUtils.join(plays,"|"),7,""}}))
                        .putIfNotEmpty("finish_keys",finish_keys)
                        .putIfNotEmpty("user_data",callId)
                        .put("areaId",state.getAreaId())
                        .build();

                RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_START, params);
                rpcCaller.invoke(sessionContext, rpcrequest);
            } catch (Throwable e) {
                logger.error("调用失败",e);
            }
        }

        businessStateService.updateInnerField(callId, IVRActionService.IVR_NEXT_FIELD,next);
        return true;
    }
}
