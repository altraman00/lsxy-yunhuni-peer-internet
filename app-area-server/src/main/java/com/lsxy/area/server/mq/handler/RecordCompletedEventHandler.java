package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.agentserver.RecordCompletedEvent;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

/**
 * 处理录音完成事件
 * Created by liuws on 2016/9/13.
 */
@Component
public class RecordCompletedEventHandler implements MQMessageHandler<RecordCompletedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(RecordCompletedEventHandler.class);

    /**录音文件每秒8KB**/
    private static final int SIZE_PRESECOND = 8 * 1024;

    @Autowired
    private VoiceFileRecordService voiceFileRecordService;
    @Autowired
    private CalCostService calCostService;

    private long getTimelong(long start,long end){
        long second = end - start;
        if(second < 0){
            return 0;
        }
        return second;
    }

    private long getRecordSize(long start,long end){
        return SIZE_PRESECOND * getTimelong(start,end);
    }

    @Override
    public void handleMessage(RecordCompletedEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("处理录音完成事件{}",message.toJson());
        }
        if(message == null){
            return;
        }
        if(message.getTenantId() == null){
            return;
        }
        if(message.getAppId() == null){
            return;
        }
        if(message.getAreaId() == null){
            return;
        }
        if(message.getCallId() == null){
            return;
        }
        if(message.getUrl() == null){
            return;
        }
        if(message.getStarTime() == null){
            return;
        }
        if(message.getEndTime() == null){
            return;
        }
        VoiceFileRecord record = new VoiceFileRecord();
        if(message.getRecordId()!=null){
            record.setId(message.getRecordId());
        }
        record.setTenantId(message.getTenantId());
        record.setAppId(message.getAppId());
        record.setAreaId(message.getAreaId());
        record.setSessionId(message.getCallId());
        record.setSessionCode(message.getType());
        record.setUrl(message.getUrl());
        record.setCallTimeLong(getTimelong(message.getStarTime(),message.getEndTime()));
        // 录音扣费金额,录音扣费时长
        calCostService.recordConsumeCal(record);
        record.setSize(getRecordSize(message.getStarTime(),message.getEndTime()));
        voiceFileRecordService.insertRecord(record);

    }
}
