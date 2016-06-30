package com.lsxy.yunhuni.callrecord.dao;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.callrecord.model.CallRecord;

import java.io.Serializable;
import java.sql.Date;

/**
 * Created by liups on 2016/6/29.
 */
public interface CallRecordDao extends BaseDaoInterface<CallRecord, Serializable> {
    Long countByAppAndCallEndTimeBetween(App app, Date start, Date end);
}
