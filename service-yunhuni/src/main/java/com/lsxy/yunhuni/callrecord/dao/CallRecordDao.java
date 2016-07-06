package com.lsxy.yunhuni.callrecord.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.callrecord.model.CallRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * 呼叫记录查询类
 * Created by liups on 2016/6/29.
 */
public interface CallRecordDao extends BaseDaoInterface<CallRecord, Serializable> {
    /**
     * 统计一个应用一定时间内的呼叫记录
     * @param app 应用
     * @param start 开始时间
     * @param end 结束时间
     * @return 次数
     */
    Long countByAppAndCallEndTimeBetween(App app, Date start, Date end);
}
