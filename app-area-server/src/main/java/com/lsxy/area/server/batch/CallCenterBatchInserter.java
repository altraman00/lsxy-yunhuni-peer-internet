package com.lsxy.area.server.batch;

import com.alibaba.dubbo.config.annotation.Reference;
import com.lsxy.call.center.api.model.CallCenter;
import com.lsxy.call.center.api.service.CallCenterService;
import com.lsxy.framework.api.base.AsyncBatchInserter;
import com.lsxy.framework.api.base.BaseService;
import org.springframework.stereotype.Component;

/**
 * Created by liuws on 2016/12/13.
 */
@Component
public class CallCenterBatchInserter extends AsyncBatchInserter<CallCenter>{

    @Reference(lazy = true,check = false,timeout = 6000)
    private CallCenterService callCenterService;

    @Override
    public BaseService<CallCenter> getBaseService() {
        return callCenterService;
    }
}
