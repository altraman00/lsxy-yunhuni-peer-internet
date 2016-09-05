package com.lsxy.framework.mq.events.oc;

import com.lsxy.framework.mq.api.AbstractMQEvent;

import java.util.List;

/**
 * 放音文件同步完成
 * Created by zhangxb on 2016/09/05.
 */
public class VoiceFilePlaySyncOkEvent extends AbstractMQEvent{

    private List files;      //放音文件id

    public VoiceFilePlaySyncOkEvent(List files){
        this.files = files;
    }

    public List getFiles() {
        return files;
    }

    public void setFiles(List files) {
        this.files = files;
    }

    @Override
    public String getTopicName() {
        return "yunhuni_topic_oc";
    }
}
