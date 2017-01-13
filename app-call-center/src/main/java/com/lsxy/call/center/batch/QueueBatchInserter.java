package com.lsxy.call.center.batch;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.framework.api.base.AsyncBatchInserter;
import com.lsxy.framework.api.base.BaseService;
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
public class QueueBatchInserter extends AsyncBatchInserter<CallCenterQueue>{

    @Autowired
    private CallCenterQueueService callCenterQueueService;

    @Override
    public BaseService<CallCenterQueue> getBaseService() {
        return callCenterQueueService;
    }
}
