package com.lsxy.area.server.batch;

import com.lsxy.framework.api.base.AsyncBatchInserter;
import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.session.model.VoiceIvr;
import com.lsxy.yunhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.api.session.service.VoiceIvrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuws on 2016/12/13.
 */
@Component
public class VoiceIvrBatchInserter extends AsyncBatchInserter<VoiceIvr> {

    @Autowired
    private VoiceIvrService voiceIvrService;

    @Override
    public BaseService<VoiceIvr> getBaseService() {
        return voiceIvrService;
    }
}

