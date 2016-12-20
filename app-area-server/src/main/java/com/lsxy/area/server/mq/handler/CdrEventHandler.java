package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.mq.CdrEvent;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.framework.core.utils.JSONUtil2;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.callcenter.CallCenterIncrCostEvent;
import com.lsxy.yunhuni.api.product.service.CalCostService;
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
    private CalCostService calCostService;

    @Autowired
    private VoiceCdrService voiceCdrService;

    @Autowired
    private CalBillingService calBillingService;

    @Override
    public void handleMessage(CdrEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理cdr事件{}",message.toJson());
        }
        VoiceCdr voiceCdr = null;
        try{
            voiceCdr = JSONUtil2.fromJson(message.getVoiceCdr(),VoiceCdr.class);
        }catch (Throwable t){
            logger.error("cdr反序列化失败{}",t);
        }
        String callCenterId = message.getCallCenterId();
        //扣费
        if(voiceCdr.getCallAckDt() != null){
            calCostService.callConsume(voiceCdr);
        }else{
            voiceCdr.setCostTimeLong(0L);
            voiceCdr.setCost(BigDecimal.ZERO);
            voiceCdr.setDeduct(0L);
            voiceCdr.setCostType(VoiceCdr.COST_TYPE_COST);
        }

        if(logger.isDebugEnabled()){
            logger.debug("插入cdr数据： {}", JSONUtil.objectToJson(voiceCdr));
        }
        calBillingService.incCallSum(voiceCdr.getTenantId(),voiceCdr.getCallEndDt());
        if(voiceCdr.getCallAckDt() != null){
            calBillingService.incCallConnect(voiceCdr.getTenantId(),voiceCdr.getCallEndDt());
        }
        calBillingService.incCallCostTime(voiceCdr.getTenantId(),voiceCdr.getCallEndDt(),voiceCdr.getCostTimeLong());
        voiceCdrService.save(voiceCdr);

        if(callCenterId!=null){
            if(voiceCdr.getCost() != null && voiceCdr.getCost().compareTo(BigDecimal.ZERO) == 1){
                mqService.publish(new CallCenterIncrCostEvent(callCenterId,voiceCdr.getCost()));
            }
        }
    }
}
