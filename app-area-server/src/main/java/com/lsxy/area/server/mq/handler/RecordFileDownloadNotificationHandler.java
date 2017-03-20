package com.lsxy.area.server.mq.handler;

import com.lsxy.area.server.util.NotifyCallbackUtil;
import com.lsxy.framework.core.utils.MapBuilder;
import com.lsxy.framework.core.utils.OssTempUriUtils;
import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.apigw.RecordFileDownloadNotificationEvent;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import java.util.Map;

/**
 * Created by liups on 2017/3/20.
 */
public class RecordFileDownloadNotificationHandler implements MQMessageHandler<RecordFileDownloadNotificationEvent> {
    private static final Logger logger = LoggerFactory.getLogger(RecordFileDownloadNotificationHandler.class);
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    MQService mqService;
    @Autowired
    private NotifyCallbackUtil notifyCallbackUtil;
    @Autowired
    private ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    private AppService appService;

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
            String callbackUrl = null;
            String subaccountId = message.getSubaccountId();
            if(StringUtils.isNotBlank(subaccountId)){
                ApiCertificateSubAccount subAccount = apiCertificateSubAccountService.findById(subaccountId);
                String subCallbackUrl = subAccount.getCallbackUrl();
                if(StringUtils.isNotBlank(subCallbackUrl)){
                    callbackUrl = subCallbackUrl;
                }
            }
            if(StringUtils.isNotBlank(callbackUrl)){
                App app = appService.findById(message.getAppId());
                String url = app.getUrl();
                if(StringUtils.isNotBlank(url)){
                    callbackUrl = url;
                }
            }
            if(StringUtils.isBlank(callbackUrl)){
                logger.error("缺少回调地址");
                return;
            }
            Map<String,Object> notify_data = new MapBuilder<String,Object>()
                    .putIfNotEmpty("event","recording.download_preparing")
                    .putIfNotEmpty("id",message.getRecordFileId())
                    .putIfNotEmpty("subaccount_id",message.getSubaccountId())
                    .putIfNotEmpty("url",ossUri)
                    .build();
            // 发送通知
            notifyCallbackUtil.postNotify(callbackUrl,notify_data,null,3);
        }else{
            mqService.publish(new RecordFileDownloadNotificationEvent(message.getAppId(),message.getSubaccountId(),voiceFileRecord.getId(),message.getCount() + 1));
        }
    }
}
