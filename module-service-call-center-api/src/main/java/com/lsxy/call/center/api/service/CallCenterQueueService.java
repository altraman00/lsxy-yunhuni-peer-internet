package com.lsxy.call.center.api.service;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.framework.api.base.BaseService;
import java.util.List;

/**
 * Created by liuws on 2016/11/14.
 */
public interface CallCenterQueueService extends BaseService<CallCenterQueue> {

    public void update(String id,CallCenterQueue queue);
}
