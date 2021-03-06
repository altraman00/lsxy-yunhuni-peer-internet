package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.mq.CdrEvent;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.CallCenterIncrCostEvent;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.math.BigDecimal;

/**
 * 处理CDR消息
 * Created by liuws on 2016/9/13.
 */
@Component
public class CdrEventHandler implements MQMessageHandler<CdrEvent> {

    private static final Logger logger = LoggerFactory.getLogger(CdrEventHandler.class);

    @Autowired
    private MQService mqService;

    @Autowired
    private VoiceCdrService voiceCdrService;

    @Override
    public void handleMessage(CdrEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理cdr事件{}",message.toJson());
        }
        VoiceCdr voiceCdr = null;
        try{
            voiceCdr = JSONUtil2.fromJson(message.getVoiceCdr(),VoiceCdr.class);
        }catch (Throwable t){
            logger.error(String.format("cdr反序列化失败,cdr=%s",message.getVoiceCdr()),t);
        }
        if(voiceCdr == null){
            return;
        }
        voiceCdrService.insertCdr(voiceCdr);
        if(message.getCallCenterId()!=null){
            if(voiceCdr.getCost() != null && voiceCdr.getCost().compareTo(BigDecimal.ZERO) == 1){
                mqService.publish(new CallCenterIncrCostEvent(message.getCallCenterId(),voiceCdr.getCost()));
            }
        }
    }
}
