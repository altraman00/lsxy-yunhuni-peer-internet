package com.lsxy.area.server.batch;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.model.CallCenterConversation;
import com.lsxy.call.center.api.service.CallCenterConversationService;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.base.AsyncBatchInserter;
import com.lsxy.framework.api.base.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by liuws on 2016/12/13.
 */
@Component
public class CallCenterConversationBatchInserter extends AsyncBatchInserter<CallCenterConversation>{

    @Reference(lazy = true,check = false,timeout = 6000)
    private CallCenterConversationService callCenterConversationService;

    @Override
    public BaseService<CallCenterConversation> getBaseService() {
        return callCenterConversationService;
    }
}
