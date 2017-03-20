package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.core.utils.OssTempUriUtils;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.RecordFileDownloadNotificationEvent;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;

/**
 * Created by liups on 2017/3/20.
 */
public class RecordFileDownloadNotificationHandler implements MQMessageHandler<RecordFileDownloadNotificationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileDownloadNotificationHandler.class);
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    MQService mqService;
    @Override
    public void handleMessage(RecordFileDownloadNotificationEvent message) throws JMSException {
        if(logger.isDebugEnabled()){
            logger.debug("下载通知处理");
        }
        if(message.getCount()>5){
            return;
        }
        if(StringUtils.isBlank(message.getAppId()) || StringUtils.isBlank(message.getRecordFileId())){
            logger.error("缺少必要的数据");
            return;
        }
        VoiceFileRecord voiceFileRecord = voiceFileRecordService.findById(message.getRecordFileId());

        if(voiceFileRecord.getStatus()!=null&&voiceFileRecord.getStatus()==1){
            String ossUri = OssTempUriUtils.getOssTempUri(voiceFileRecord.getOssUrl());
            if(logger.isDebugEnabled()) {
                logger.debug("生成ossUri地址：[{}]", ossUri);
            }
            //TODO 发送通知

        }else{
            mqService.publish(new RecordFileDownloadNotificationEvent(message.getAppId(),message.getSubaccountId(),voiceFileRecord.getId(),message.getCount() + 1));
        }
    }
}
