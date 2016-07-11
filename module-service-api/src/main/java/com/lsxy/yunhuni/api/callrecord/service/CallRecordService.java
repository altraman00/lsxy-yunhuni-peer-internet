package com.lsxy.yunhuni.api.callrecord.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.callrecord.model.CallRecord;

import java.util.Map;

/**
 * 呼叫记录相关接口
 * Created by liups on 2016/6/29.
 */
public interface CallRecordService extends BaseService<CallRecord> {
    /**
     * 根据appId统计当前（有一定延迟）呼叫记录
     * @param appId 应用ID
     * @return
     */
    Map currentRecordStatistics(String appId);
}
