package com.lsxy.area.server.mq.handler;

import com.lsxy.framework.mq.api.MQMessageHandler;
import com.lsxy.framework.mq.events.oc.VoiceFilePlaySyncOkEvent;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import com.lsxy.yunhuni.api.file.service.VoiceFilePlayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by tandy on 16/8/29.
 * 放音文件审核成功以后,通知所有区域到OSS下载对应的放音文件
 */
@Component
public class VoiceFilePlaySyncOkEventHandler implements MQMessageHandler<VoiceFilePlaySyncOkEvent>{

    private static final Logger logger = LoggerFactory.getLogger(VoiceFilePlaySyncOkEventHandler.class);

    @Autowired
    private VoiceFilePlayService voiceFilePlayService;

    @Override
    public void handleMessage(VoiceFilePlaySyncOkEvent event) throws JMSException {
        List<Map> list = event.getFiles();
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
    }
}
