package com.lsxy.call.center.service;

import com.lsxy.call.center.api.model.CallCenterQueue;
import com.lsxy.call.center.api.service.CallCenterQueueService;
import com.lsxy.call.center.dao.CallCenterQueueDao;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liuws on 2016/11/14.
 */
@Service
@com.alibaba.dubbo.config.annotation.Service
public class CallCenterQueueServiceImpl extends AbstractService<CallCenterQueue> implements CallCenterQueueService {

    private static final Logger logger = LoggerFactory.getLogger(CallCenterQueueServiceImpl.class);

    @Autowired
    private CallCenterQueueDao callCenterQueueDao;


    @Override
    public BaseDaoInterface<CallCenterQueue, Serializable> getDao() {
        return callCenterQueueDao;
    }

    @Override
    public void update(String id,CallCenterQueue queue){
        if(queue == null){
            return;
        }
        CallCenterQueue q = this.findById(id);
        if(q == null){
            return;
        }
        try {
            BeanUtils.copyProperties2(q,queue,false);
        } catch (Throwable e) {
            logger.error("更新排队记录失败",e);
        }
        this.save(q);
    }
}
