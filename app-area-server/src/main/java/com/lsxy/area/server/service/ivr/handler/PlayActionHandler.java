package com.lsxy.area.server.service.ivr.handler;

import com.lsxy.area.api.BusinessState;
import com.lsxy.area.api.BusinessStateService;
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
import java.util.HashMap;
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
    public boolean handle(String callId, Element root,String next) {
        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr动作，callId={},ivr={}",callId,getAction());
        }
        BusinessState state = businessStateService.get(callId);
        if(state == null){
            logger.info("没有找到call_id={}的state",callId);
            return false;
        }
        String finish_keys = root.attributeValue("finish_keys");
        String repeat = root.attributeValue("repeat");
        List<String> plays = new ArrayList<String>();
        if(StringUtils.isNotBlank(root.getTextTrim())){
            plays.add(root.getTextTrim());
        }

        if(logger.isDebugEnabled()){
            logger.debug("开始处理ivr[{}]动作，finish_keys={},repeat={},play={}",
                    getAction(),finish_keys,repeat,plays);
        }
        Map<String,Object> businessData = state.getBusinessData();
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

        if(businessData == null){
            businessData = new HashMap<>();
        }
        businessData.put("next",next);
        state.setBusinessData(businessData);
        businessStateService.save(state);
        return true;
    }
}
