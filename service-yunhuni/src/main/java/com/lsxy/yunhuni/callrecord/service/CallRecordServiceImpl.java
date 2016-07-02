package com.lsxy.yunhuni.callrecord.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yuhuni.api.app.model.App;
import com.lsxy.yuhuni.api.callrecord.model.CallRecord;
import com.lsxy.yuhuni.api.callrecord.service.CallRecordService;
import com.lsxy.yuhuni.api.session.service.CallSessionService;
import com.lsxy.yunhuni.callrecord.dao.CallRecordDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class CallRecordServiceImpl extends AbstractService<CallRecord> implements CallRecordService {

    @Autowired
    private CallRecordDao callRecordDao;

    @Autowired
    private CallSessionService callSessionService;

    @Override
    public BaseDaoInterface<CallRecord, Serializable> getDao() {
        return this.callRecordDao;
    }

    @Override
    public Map currentRecordStatistics(String appId) {
        //结束时间为准
        App app = new App();
        app.setId(appId);
        //当天记录
        String date = DateUtils.getDate("yyyy-MM-dd");
        Date today = DateUtils.parseDate(date);
        Date nextDate = DateUtils.nextDate(today);
        Long dayCount = callRecordDao.countByAppAndCallEndTimeBetween(app, new java.sql.Date(today.getTime()), new java.sql.Date(nextDate.getTime()));

        //前一小时
        String hour = DateUtils.getDate("yyyy-MM-dd HH");
        Date currentHour = DateUtils.parseDate(hour,"yyyy-MM-dd HH");
        Date lastHour = new Date(currentHour.getTime() - 3600000);
        Long hourCount = callRecordDao.countByAppAndCallEndTimeBetween(app, new java.sql.Date(lastHour.getTime()), new java.sql.Date(currentHour.getTime()));

        Long currentSession = callSessionService.currentCallSessionCount(appId);

        Map result = new HashMap();
        result.put("dayCount",dayCount);
        result.put("hourCount",hourCount);
        result.put("currentSession",currentSession);
        return result;
    }
}
