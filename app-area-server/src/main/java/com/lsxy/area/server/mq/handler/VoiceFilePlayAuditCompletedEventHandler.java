package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.rpc.api.RPCCaller;
import com.lsxy.framework.rpc.api.RPCRequest;
import com.lsxy.framework.rpc.api.ServiceConstants;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import com.lsxy.framework.rpc.exceptions.RightSessionNotFoundExcepiton;
import com.lsxy.framework.rpc.exceptions.SessionWriteException;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

import static com.sun.corba.se.impl.util.Utility.printStackTrace;

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
        String param = "fileid="+event.getId();
        VoiceFilePlay vfp = voiceFilePlayService.findById(event.getId());

        RPCRequest request = RPCRequest.newRequest(ServiceConstants.MN_CH_VF_SYNC,param);
        try {
            rpcCaller.invoke(sessionContext,request);
        } catch (Exception ex) {
            logger.error("发送放音文件指令失败:"+request,ex);
        }

    }
}
