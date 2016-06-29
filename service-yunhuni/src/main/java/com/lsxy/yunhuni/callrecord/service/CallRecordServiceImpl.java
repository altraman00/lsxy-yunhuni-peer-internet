package com.lsxy.yunhuni.callrecord.service;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.yuhuni.app.model.App;
import com.lsxy.yuhuni.callrecord.model.CallRecord;
import com.lsxy.yuhuni.callrecord.service.CallRecordService;
import com.lsxy.yunhuni.callrecord.dao.CallRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class CallRecordServiceImpl extends AbstractService<CallRecord> implements CallRecordService {

    @Autowired
    CallRecordDao callRecordDao;

    @Override
    public BaseDaoInterface<CallRecord, Serializable> getDao() {
        return this.callRecordDao;
    }

    @Override
    public Map currentRecordStatistics(String appId) {
        //结束时间为准
        App app = new App();
        app.setId(appId);
        //

        return null;
    }
}
