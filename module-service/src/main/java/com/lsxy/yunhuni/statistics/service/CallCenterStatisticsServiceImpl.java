package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import com.lsxy.yunhuni.statistics.dao.CallCenterStatisticsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * Created by liups on 2016/11/7.
 */
@Service
public class CallCenterStatisticsServiceImpl extends AbstractService<CallCenterStatistics> implements CallCenterStatisticsService{
    private static final Logger logger = LoggerFactory.getLogger(CallCenterStatisticsServiceImpl.class);

    String CC_STATISTICS_TENANT_PREFIX = "CC_STATISTICS_TENANT_";
    String CC_STATISTICS_APP_PREFIX = "CC_STATISTICS_APP_";

    @Autowired
    CallCenterStatisticsDao callCenterStatisticsDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    AppService appService;
    @Autowired
    RedisCacheService redisCacheService;

    @Override
    public BaseDaoInterface<CallCenterStatistics, Serializable> getDao() {
        return this.callCenterStatisticsDao;
    }

    @Override
    public void dayStatistics(Date date) {
        String yyyyMMdd = DateUtils.formatDate(date, "yyyyMMdd");
        Date statisticsDate = DateUtils.parseDate(yyyyMMdd,"yyyyMMdd");
        Iterable<Tenant> tenants = tenantService.list();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        for(Tenant tenant:tenants){
            results.add(executorService.submit(() -> this.statisticsTenantAndApp(statisticsDate, tenant)));
        }
        for(Future f : results){
            try {
                f.get();
            }catch (Throwable t){

            }
        }
        executorService.shutdown();
    }


    @Override
    public void statisticsTenantAndApp(Date statisticsDate, Tenant tenant) {
        CallCenterStatistics tenantStatistics = callCenterStatisticsDao.findFirstByTenantIdAndAppIdIsNullOrderByDtDesc(tenant.getId());
        if(tenantStatistics != null){
            tenantStatistics(tenantStatistics,statisticsDate);
        }else{
            Date createTime = tenant.getCreateTime();
            Date preCreateDate = DateUtils.getPreDate(createTime);
            String preCreateDateStr = DateUtils.formatDate(preCreateDate, "yyyyMMdd");
            Date dt = DateUtils.parseDate(preCreateDateStr,"yyyyMMdd");
            tenantStatistics = new CallCenterStatistics(tenant.getId(),null,dt, 0L, 0L,0L,0L,0L,0L,0L);
            tenantStatistics(tenantStatistics,statisticsDate);
        }
        List<App> apps = appService.getAppsByTenantId(tenant.getId());
        for(App app:apps){
            CallCenterStatistics appStatistics = callCenterStatisticsDao.findFirstByAppIdOrderByDtDesc(app.getId());
            if(appStatistics != null){
                appStatistics(appStatistics,statisticsDate);
            }else{
                Date appCreateTime = app.getCreateTime();
                Date preAppCreateTime = DateUtils.getPreDate(appCreateTime);
                String preAppCreateTimeStr = DateUtils.formatDate(preAppCreateTime, "yyyyMMdd");
                Date appDt = DateUtils.parseDate(preAppCreateTimeStr,"yyyyMMdd");
                appStatistics = new CallCenterStatistics(tenant.getId(),app.getId(),appDt, 0L, 0L,0L,0L,0L,0L,0L);
                appStatistics(appStatistics,statisticsDate);
            }
        }
    }

    private void tenantStatistics(CallCenterStatistics lastStatistics,Date date){
        Date lastStatisticsDate = lastStatistics.getDt();
        String tenantId = lastStatistics.getTenantId();
        if(lastStatisticsDate.getTime() < date.getTime()){
            Date startDate = DateUtils.nextDate(lastStatisticsDate);
            Date endDate = DateUtils.nextDate(startDate);
            //TODO 获取统计日期内的数据
            CallCenterStatistics current = new CallCenterStatistics(tenantId,null,startDate,0L, 0L,0L,0L,0L,0L,0L);
            this.save(current);
            tenantStatistics(current,date);
        }
    }


    private void appStatistics(CallCenterStatistics lastStatistics,Date date){
        Date lastStatisticsDate = lastStatistics.getDt();
        String tenantId = lastStatistics.getTenantId();
        String appId = lastStatistics.getAppId();
        if(lastStatisticsDate.getTime() < date.getTime()){
            Date startDate =  DateUtils.nextDate(lastStatisticsDate);
            Date endDate = DateUtils.nextDate(startDate);
            //TODO 获取统计日期内的数据

            CallCenterStatistics current = new CallCenterStatistics(tenantId,null,startDate,0L, 0L,0L,0L,0L,0L,0L);
            this.save(current);
            appStatistics(current,date);
        }
    }

    @Override
    public CallCenterStatistics getStatisticsByTenantId(String tenantId, Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        Date dt = DateUtils.parseDate(dateStr,"yyyyMMdd");
        CallCenterStatistics dayStatistics = callCenterStatisticsDao.findFirstByTenantIdAndDtAndAppIdIsNull(tenantId,dt);
        if(dayStatistics==null){
            dayStatistics = callCenterStatisticsDao.findFirstByTenantIdAndDtLessThanAndAppIdIsNullOrderByDtDesc(tenantId,dt);
        }
        return dayStatistics;
    }

    @Override
    public void incrIntoRedis(CallCenterStatistics callCenterStatistics,Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String tenantKey = CC_STATISTICS_TENANT_PREFIX + callCenterStatistics.getTenantId() + "_" + dateStr;
        String appKey = CC_STATISTICS_APP_PREFIX + callCenterStatistics.getAppId() + "_" + dateStr;
        incrIntoRedis(tenantKey,callCenterStatistics);
        incrIntoRedis(appKey,callCenterStatistics);
    }

    private void incrIntoRedis(String key,CallCenterStatistics ccStatistics){
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        long callIn = hashOps.increment("callIn", ccStatistics.getCallIn());
        hashOps.increment("callInSuccess", ccStatistics.getCallInSuccess());
        long callOut = hashOps.increment("callOut", ccStatistics.getCallOut());
        hashOps.increment("callOutSuccess", ccStatistics.getCallOutSuccess());
        hashOps.increment("toManualSuccess", ccStatistics.getToManualSuccess());
        hashOps.increment("queueNum", ccStatistics.getQueueNum());
        hashOps.increment("queueDuration", ccStatistics.getQueueDuration());
        if(callIn == ccStatistics.getCallIn() || callOut == ccStatistics.getCallOut()){
            redisCacheService.expire(key, 5 * 24 * 60 * 60);
        }
    }



    private CallCenterStatistics getIncrFromRedisByTenantId(String tenantId,Date date){
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String tenantKey = CC_STATISTICS_TENANT_PREFIX + tenantId + "_" + dateStr;
        return getIncrFromRedis(tenantKey);
    }

    private CallCenterStatistics getIncrFromRedisByAppId(String appId,Date date){
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String tenantKey = CC_STATISTICS_APP_PREFIX + appId + "_" + dateStr;
        return getIncrFromRedis(tenantKey);
    }

    private CallCenterStatistics getIncrFromRedis(String key){
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Map entries = hashOps.entries();
        CallCenterStatistics current = new CallCenterStatistics(null,null,null,0L, 0L,0L,0L,0L,0L,0L);
        try {
            BeanUtils.copyProperties2(current,entries,false);
        } catch (Exception e) {
            logger.error("复制对象属性出错",e);
        }
        return current;
    }


}
