package com.lsxy.area.server.mq.handler;

import com.alibaba.fastjson.JSON;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功以后,通知所有区域到OSS下载对应的放音文件
 */
@Component
public class VoiceFilePlayAuditCompletedEventHandler implements MQMessageHandler<VoiceFilePlayAuditCompletedEvent>{

    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlayAuditCompletedEventHandler.class);

    @Autowired
    private ServerSessionContext sessionContext;

    @Autowired
    private RPCCaller rpcCaller;

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    @Override
    public void handleMessage(VoiceFilePlayAuditCompletedEvent event) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("放音文件同步开启");
        }
        List<VoiceFilePlay> list = voiceFilePlayService.findNotSync();
        List<Map<String,Object>> list1 = new ArrayList<>();
        for(int i=0;i<list.size();i++) {
            Map<String, Object> map = new HashMap();
            map.put("id",list.get(i).getId());
            map.put("appId",list.get(i).getApp().getId());
            map.put("tenantId",list.get(i).getTenant().getId());
            map.put("name",list.get(i).getName());
            map.put("fileKey",list.get(i).getFileKey());
            list1.add(map);
        }
        String param = JSON.toJSON(list1).toString();
        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_SYNC,param);
        try {
            rpcCaller.invoke(sessionContext,request);
            logger.info("发送放音文件指令成功");
        } catch (Exception ex) {
            logger.error("发送放音文件指令失败:"+request,ex);
        }

    }
}
