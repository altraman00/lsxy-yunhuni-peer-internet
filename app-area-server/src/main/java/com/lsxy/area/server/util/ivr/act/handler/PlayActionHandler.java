package com.lsxy.area.server.util.ivr.act.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
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

    @Override
    public String getAction() {
        return "play";
    }

    @Override
    public boolean handle(String callId, Element root) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},act={}",callId,getAction());
        }
        String finish_keys = root.attributeValue("finish_keys");
        String repeat = root.attributeValue("repeat");
        List<String> plays = new ArrayList<String>();
        if(StringUtils.isNotBlank(root.getTextTrim())){
            plays.add(root.getTextTrim());
        }
        String nextUrl = "";
        Element next = root.element("next");
        if(next!=null){
            if(StringUtils.isNotBlank(next.getTextTrim())){
                nextUrl = next.getTextTrim();
            }
        }
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作，finish_keys={},repeat={},play={}",
                            getAction(),finish_keys,repeat,plays);
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }
        Map<String,Object> businessData = state.getBusinessData();
        String res_id = state.getResId();
        if(plays!=null && plays.size()>0){
            Map<String, Object> params = new MapBuilder<String,Object>()
                    .putIfNotEmpty("res_id",res_id)
                    .putIfNotEmpty("content", JSONUtil2.objectToJson(new Object[][]{new Object[]{StringUtils.join(plays,"|"),7,""}}))
                    .putIfNotEmpty("finish_keys",finish_keys)
                    .putIfNotEmpty("user_data",callId)
                    .build();

            RPCRequest rpcrequest = RPCRequest.newRequest(ServiceConstants.MN_CH_SYS_CALL_PLAY_START, params);
            try {
                rpcCaller.invoke(sessionContext, rpcrequest);
            } catch (Throwable e) {
                logger.error("调用失败",e);
            }
        }

        if(businessData == null){
            businessData = new HashMap<>();
        }
        businessData.put("next",nextUrl);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        return true;
    }
}
