package com.lsxy.area.server.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.RPCResponse;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.handler.RpcRequestHandler;
import com.lsxy.framework.rpc.api.session.Session;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功以后,通知所有区域到OSS下载对应的放音文件
 */
@Component
public class Handler_MN_CH_VF_SYNC_OK extends RpcRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(Handler_MN_CH_VF_SYNC_OK.class);

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    @Override
    public String getEventName() {
        return ServiceConstants.MN_CH_VF_SYNC_OK;
    }

    @Override
    public RPCResponse handle(RPCRequest request, Session session) {
        if(logger.isDebugEnabled()){
            logger.debug("响应VF SYNC OK:{}",request);
        }
        String jsonList = request.getBodyAsString();
        //String jsonList = (String) request.getParameter(ServiceConstants.MN_CH_VF_SYNC_OK);
        List<Map> list = JSON.parseArray(jsonList, Map.class);
        List<String> success = new ArrayList<>();
        List<String> fail = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Map<String,String> vfp = list.get(i);
            if(VoiceFilePlay.STATUS_SUCCESS==Integer.valueOf(vfp.get("sync"))){
                success.add(vfp.get("id"));
            }else if(VoiceFilePlay.SYNC_FAIL==Integer.valueOf(vfp.get("sync"))){
                fail.add(vfp.get("id"));
            }
        }
        voiceFilePlayService.batchUpdateSync(success,VoiceFilePlay.SYNC_SUCCESS);
        voiceFilePlayService.batchUpdateSync(fail,VoiceFilePlay.SYNC_FAIL);
        return null;
    }
}
