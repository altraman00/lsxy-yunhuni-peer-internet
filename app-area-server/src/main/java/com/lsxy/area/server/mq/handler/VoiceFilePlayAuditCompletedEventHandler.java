package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.oc.VoiceFilePlayAuditCompletedEvent;
import com.lsxy.framework.rpc.api.SessionContext;
import com.lsxy.framework.rpc.api.server.ServerSessionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功以后,通知所有区域到OSS下载对应的放音文件
 */
@Component
public class VoiceFilePlayAuditCompletedEventHandler implements MQMessageHandler<VoiceFilePlayAuditCompletedEvent>{


    @Autowired
    private ServerSessionContext sessionContext;

    @Override
    public void handleMessage(VoiceFilePlayAuditCompletedEvent message) throws JMSException {
        sessionContext.sessions();
    }
}
