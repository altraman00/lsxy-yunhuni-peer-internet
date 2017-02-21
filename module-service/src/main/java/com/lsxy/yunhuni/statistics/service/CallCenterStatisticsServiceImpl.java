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
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.statistics.model.CallCenterStatistics;
import com.lsxy.yunhuni.api.statistics.service.CallCenterStatisticsService;
import com.lsxy.yunhuni.statistics.dao.CallCenterStatisticsDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
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
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    CalCostService calCostService;
    @Autowired
    ConsumeService consumeService;

    @Override
    public BaseDaoInterface<CallCenterStatistics, Serializable> getDao() {
        return this.callCenterStatisticsDao;
    }

    @Override
    public void dayStatistics(Date date) {
        String yyyyMMdd = DateUtils.formatDate(date, "yyyyMMdd");
        Date statisticsDate = DateUtils.parseDate(yyyyMMdd,"yyyyMMdd");
        Iterable<Tenant> tenants = tenantService.list();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future> results = new ArrayList<>();
        for(Tenant tenant:tenants){
//            this.statisticsTenantAndApp(statisticsDate, tenant);
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
            tenantStatistics = new CallCenterStatistics(tenant.getId(),null,dt, 0L, 0L,0L,0L,0L,0L,0L,0L);
            tenantStatistics(tenantStatistics,statisticsDate);
        }
        List<App> apps = appService.findAppByTenantIdAndServiceType(tenant.getId(),App.PRODUCT_CALL_CENTER);
        for(App app:apps){
            CallCenterStatistics appStatistics = callCenterStatisticsDao.findFirstByAppIdOrderByDtDesc(app.getId());
            if(appStatistics != null){
                appStatistics(appStatistics,statisticsDate);
            }else{
                Date appCreateTime = app.getCreateTime();
                Date preAppCreateTime = DateUtils.getPreDate(appCreateTime);
                String preAppCreateTimeStr = DateUtils.formatDate(preAppCreateTime, "yyyyMMdd");
                Date appDt = DateUtils.parseDate(preAppCreateTimeStr,"yyyyMMdd");
                appStatistics = new CallCenterStatistics(tenant.getId(),app.getId(),appDt, 0L, 0L,0L,0L,0L,0L,0L,0L);
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
            // 获取统计日期内的数据
            CallCenterStatistics statistics = statistics(tenantId, null, startDate, endDate);
            CallCenterStatistics current = new CallCenterStatistics(tenantId,null,startDate,
                    lastStatistics.getCallIn() + statistics.getCallIn(),
                    lastStatistics.getCallInSuccess() + statistics.getCallInSuccess(),
                    lastStatistics.getCallOut() + statistics.getCallOut(),
                    lastStatistics.getCallOutSuccess() + statistics.getCallOutSuccess(),
                    lastStatistics.getToManualSuccess() + statistics.getToManualSuccess(),
                    lastStatistics.getQueueNum() + statistics.getQueueNum(),
                    lastStatistics.getQueueDuration() + statistics.getQueueDuration(),
                    lastStatistics.getCallTimeLong() + statistics.getCallTimeLong());
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
            // 获取统计日期内的数据
            CallCenterStatistics statistics = statistics(tenantId, appId, startDate, endDate);
            CallCenterStatistics current = new CallCenterStatistics(tenantId,appId,startDate,
                    lastStatistics.getCallIn() + statistics.getCallIn(),
                    lastStatistics.getCallInSuccess() + statistics.getCallInSuccess(),
                    lastStatistics.getCallOut() + statistics.getCallOut(),
                    lastStatistics.getCallOutSuccess() + statistics.getCallOutSuccess(),
                    lastStatistics.getToManualSuccess() + statistics.getToManualSuccess(),
                    lastStatistics.getQueueNum() + statistics.getQueueNum(),
                    lastStatistics.getQueueDuration() + statistics.getQueueDuration(),
                    lastStatistics.getCallTimeLong() + statistics.getCallTimeLong());
            this.save(current);
            appStatistics(current,date);
        }
    }


    private CallCenterStatistics statistics(String tenantId,String appId,Date startDate,Date endDate){
        //呼叫中心
        String sql = "SELECT IFNULL(SUM(CASE WHEN c.type='1' THEN 1 ELSE 0 END),0) AS callIn, " +
                        "IFNULL(SUM(CASE WHEN (c.type='1' AND c.answer_time IS NOT NULL) THEN 1 ELSE 0 END),0) AS callInSuccess, " +
                        "IFNULL(SUM(CASE WHEN c.type='2' THEN 1 ELSE 0 END),0) AS callOut, " +
                        "IFNULL(SUM(CASE WHEN (c.type='2' AND c.answer_time IS NOT NULL) THEN 1 ELSE 0 END),0) AS callOutSuccess, " +
//                        "IFNULL(SUM(CASE WHEN c.to_manual_result=1 THEN 1 ELSE 0 END),0) AS toManualSuccess, " +
//                        "IFNULL(SUM(CASE WHEN c.to_manual_time IS NOT NULL THEN 1 ELSE 0 END),0) AS queueNum," +
//                        "IFNULL(SUM(c.to_manual_time),0) AS queueDuration, " +
                        "IFNULL(SUM(c.call_time_long),0) AS callTimeLong " +
                        "FROM db_lsxy_bi_yunhuni.tb_bi_call_center c WHERE 1=1 ";
        //排队
        String queSql = "SELECT " +
                "COUNT(1) AS queueNum," +
                "IFNULL(SUM(CASE WHEN c.result = 'dail_succ' THEN 1 ELSE 0 END),0) AS toManualSuccess, " +
                "IFNULL(SUM(c.to_manual_time),0) AS queueDuration " +
                "FROM db_lsxy_bi_yunhuni.tb_bi_call_center_queue c WHERE 1=1 ";
        if(startDate != null){
            sql = sql + "AND c.end_time >= '" + DateUtils.formatDate(startDate) + "' ";
            queSql = queSql + "AND c.end_time >= '" + DateUtils.formatDate(startDate) + "' ";
        }
        if(endDate != null){
            sql = sql + "AND c.end_time < '" + DateUtils.formatDate(endDate) + "' ";
            queSql = queSql + "AND c.end_time < '" + DateUtils.formatDate(endDate) + "' ";
        }
        if(StringUtils.isNotBlank(tenantId)){
            sql = sql + "AND c.tenant_id = '" + tenantId + "' ";
            queSql = queSql + "AND c.tenant_id = '" + tenantId + "' ";
        }
        if(StringUtils.isNotBlank(appId)){
            sql = sql + "AND c.app_id = '" + appId + "' ";
            queSql = queSql + "AND c.app_id = '" + appId + "' ";
        }
        Map map = jdbcTemplate.queryForMap(sql);
        Map queMap = jdbcTemplate.queryForMap(queSql);
        CallCenterStatistics current = new CallCenterStatistics();
        try {
            BeanUtils.copyProperties2(current,map,false);
            BeanUtils.copyProperties2(current,queMap,false);
        } catch (Exception e) {
            logger.error("复制对象属性出错",e);
        }
        return current;
    }

    @Override
    public void incrIntoRedis(CallCenterStatistics callCenterStatistics) {
        String dateStr = DateUtils.formatDate(callCenterStatistics.getDt(), "yyyyMMdd");
        String tenantKey = CC_STATISTICS_TENANT_PREFIX + callCenterStatistics.getTenantId() + "_" + dateStr;
        String appKey = CC_STATISTICS_APP_PREFIX + callCenterStatistics.getAppId() + "_" + dateStr;
        incrIntoRedis(tenantKey,callCenterStatistics);
        incrIntoRedis(appKey,callCenterStatistics);
    }


    private void incrIntoRedis(String key,CallCenterStatistics ccStatistics){
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        if(ccStatistics.getCallIn() != null && ccStatistics.getCallIn() >0){
            long callIn = hashOps.increment("callIn", ccStatistics.getCallIn());
            if(callIn == ccStatistics.getCallIn()){
                redisCacheService.expire(key, 5 * 24 * 60 * 60);
            }
        }
        if(ccStatistics.getCallInSuccess() != null && ccStatistics.getCallInSuccess() > 0){
            hashOps.increment("callInSuccess", ccStatistics.getCallInSuccess());
        }
        if(ccStatistics.getCallOut() != null && ccStatistics.getCallOut() > 0){
            long callOut = hashOps.increment("callOut", ccStatistics.getCallOut());
            if(callOut == ccStatistics.getCallOut()){
                redisCacheService.expire(key, 5 * 24 * 60 * 60);
            }
        }
        if(ccStatistics.getCallOutSuccess()!=null && ccStatistics.getCallOutSuccess()>0){
            hashOps.increment("callOutSuccess", ccStatistics.getCallOutSuccess());
        }
        if(ccStatistics.getToManualSuccess() != null && ccStatistics.getToManualSuccess() >0){
            hashOps.increment("toManualSuccess", ccStatistics.getToManualSuccess());
        }
        if(ccStatistics.getQueueNum() != null && ccStatistics.getQueueNum() >0){
            hashOps.increment("queueNum", ccStatistics.getQueueNum());
        }
        if(ccStatistics.getQueueDuration() != null && ccStatistics.getQueueDuration() > 0){
            hashOps.increment("queueDuration", ccStatistics.getQueueDuration());
        }
        if(ccStatistics.getCallTimeLong() != null && ccStatistics.getCallTimeLong() > 0){
            hashOps.increment("callTimeLong", ccStatistics.getCallTimeLong());
        }

    }

    private CallCenterStatistics getIncrFromRedis(String key){
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Map entries = hashOps.entries();
        CallCenterStatistics current = new CallCenterStatistics(null,null,null,0L, 0L,0L,0L,0L,0L,0L,0L);
        try {
            BeanUtils.copyProperties2(current,entries,false);
        } catch (Exception e) {
            logger.error("复制对象属性出错",e);
        }
        return current;
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
    public CallCenterStatistics getCurrentStatisticsByTenantId(String tenantId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date preDate = DateUtils.getPreDate(date);
        CallCenterStatistics statistics = this.getStatisticsByTenantId(tenantId,preDate);
        if(statistics == null){
            statistics = getIncrFromRedisByTenantId(tenantId,date);
        }else{
            statistics = getTenantStatistics(statistics,date);
        }
        return statistics;
    }

    private CallCenterStatistics getTenantStatistics(CallCenterStatistics statistics, Date date) {
        Date dt = statistics.getDt();
        if(dt.getTime() < date.getTime()){
            String tenantId = statistics.getTenantId();
            Date nextDate = DateUtils.nextDate(dt);
            CallCenterStatistics incr = getIncrFromRedisByTenantId(tenantId, nextDate);
            CallCenterStatistics nextDayStatics = new CallCenterStatistics(tenantId,null,nextDate,
                    statistics.getCallIn() + incr.getCallIn(),
                    statistics.getCallInSuccess() + incr.getCallInSuccess(),
                    statistics.getCallOut() + incr.getCallOut(),
                    statistics.getCallOutSuccess() + incr.getCallOutSuccess(),
                    statistics.getToManualSuccess() + incr.getToManualSuccess(),
                    statistics.getQueueNum() + incr.getQueueNum(),
                    statistics.getQueueDuration() + incr.getQueueDuration(),
                    statistics.getCallTimeLong() + incr.getCallTimeLong());
            return getTenantStatistics(nextDayStatics, date);
        }else{
            return statistics;
        }
    }


    private CallCenterStatistics getIncrFromRedisByTenantId(String tenantId,Date date){
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String tenantKey = CC_STATISTICS_TENANT_PREFIX + tenantId + "_" + dateStr;
        return getIncrFromRedis(tenantKey);
    }

    @Override
    public CallCenterStatistics getStatisticsByAppId(String appId, Date date) {
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        Date dt = DateUtils.parseDate(dateStr,"yyyyMMdd");
        CallCenterStatistics dayStatistics = callCenterStatisticsDao.findFirstByAppIdAndDt(appId,dt);
        if(dayStatistics==null){
            dayStatistics = callCenterStatisticsDao.findFirstByAppIdAndDtLessThanOrderByDtDesc(appId,dt);
        }
        return dayStatistics;
    }


    private CallCenterStatistics getIncrFromRedisByAppId(String appId,Date date){
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        String tenantKey = CC_STATISTICS_APP_PREFIX + appId + "_" + dateStr;
        return getIncrFromRedis(tenantKey);
    }

    @Override
    public CallCenterStatistics getCurrentStatisticsByAppId(String appId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date preDate = DateUtils.getPreDate(date);
        CallCenterStatistics statistics = this.getStatisticsByAppId(appId,preDate);
        if(statistics == null){
            statistics = getIncrFromRedisByAppId(appId,date);
        }else{
            statistics = getAppStatistics(statistics,date);
        }
        return statistics;
    }

    private CallCenterStatistics getAppStatistics(CallCenterStatistics statistics, Date date) {
        Date dt = statistics.getDt();
        if(dt.getTime() < date.getTime()){
            String appId = statistics.getAppId();
            Date nextDate = DateUtils.nextDate(dt);
            CallCenterStatistics incr = getIncrFromRedisByAppId(appId, nextDate);
            CallCenterStatistics nextDayStatics = new CallCenterStatistics(statistics.getTenantId(),appId,nextDate,
                    statistics.getCallIn() + incr.getCallIn(),
                    statistics.getCallInSuccess() + incr.getCallInSuccess(),
                    statistics.getCallOut() + incr.getCallOut(),
                    statistics.getCallOutSuccess() + incr.getCallOutSuccess(),
                    statistics.getToManualSuccess() + incr.getToManualSuccess(),
                    statistics.getQueueNum() + incr.getQueueNum(),
                    statistics.getQueueDuration() + incr.getQueueDuration(),
                    statistics.getCallTimeLong() + incr.getCallTimeLong());
            return getAppStatistics(nextDayStatics, date);
        }else{
            return statistics;
        }
    }

    @Override
    public CallCenterStatistics getIncStaticsOfCurrentMonthByTenantId(String tenantId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date firstTimeOfMonth = DateUtils.getFirstTimeOfMonth(date);
        CallCenterStatistics result;
        if(date.equals(firstTimeOfMonth)){
            result = getIncrFromRedisByTenantId(tenantId,date);
        }else{
            CallCenterStatistics current = getCurrentStatisticsByTenantId(tenantId);
            Date preMonthDate = DateUtils.getPreDate(firstTimeOfMonth);
            CallCenterStatistics preMonth = this.getStatisticsByTenantId(tenantId,preMonthDate);
            if(current != null && preMonth != null){
                preMonth = getTenantStatistics(preMonth,preMonthDate);
                result = new CallCenterStatistics(tenantId,null,date,
                        current.getCallIn() - preMonth.getCallIn(),
                        current.getCallInSuccess() - preMonth.getCallInSuccess(),
                        current.getCallOut() - preMonth.getCallOut(),
                        current.getCallOutSuccess() - preMonth.getCallOutSuccess(),
                        current.getToManualSuccess() - preMonth.getToManualSuccess(),
                        current.getQueueNum() - preMonth.getQueueNum(),
                        current.getQueueDuration() - preMonth.getQueueDuration(),
                        current.getCallTimeLong() - preMonth.getCallTimeLong());
            }else if(current != null){
                result = current;
            }else{
                result = getIncrFromRedisByTenantId(tenantId,date);
            }
        }
        return result;
    }

    @Override
    public CallCenterStatistics getIncStaticsOfCurrentMonthByAppId(String AppId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date firstTimeOfMonth = DateUtils.getFirstTimeOfMonth(date);
        CallCenterStatistics result;
        if(date.equals(firstTimeOfMonth)){
            result = getIncrFromRedisByAppId(AppId,date);
        }else{
            CallCenterStatistics current = getCurrentStatisticsByAppId(AppId);
            Date preMonthDate = DateUtils.getPreDate(firstTimeOfMonth);
            CallCenterStatistics preMonth = this.getStatisticsByAppId(AppId,preMonthDate);
            if(current != null && preMonth != null){
                preMonth = getAppStatistics(preMonth,preMonthDate);
                result = new CallCenterStatistics(preMonth.getTenantId(),AppId,date,
                        current.getCallIn() - preMonth.getCallIn(),
                        current.getCallInSuccess() - preMonth.getCallInSuccess(),
                        current.getCallOut() - preMonth.getCallOut(),
                        current.getCallOutSuccess() - preMonth.getCallOutSuccess(),
                        current.getToManualSuccess() - preMonth.getToManualSuccess(),
                        current.getQueueNum() - preMonth.getQueueNum(),
                        current.getQueueDuration() - preMonth.getQueueDuration(),
                        current.getCallTimeLong() - preMonth.getCallTimeLong());
            }else if(current != null){
                result = current;
            }else{
                result = getIncrFromRedisByAppId(AppId,date);
            }
        }
        return result;
    }

    @Override
    public void agentMonthTask(){
        Date curTime = new Date();
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH,-timeLong);
        cal.set(Calendar.HOUR_OF_DAY, 0);//时
        cal.set(Calendar.MINUTE, 0);//分
        cal.set(Calendar.SECOND, 0);//秒
        cal.set(Calendar.MILLISECOND,0);//毫秒
        //获取删除时间
        Date endTime =  cal.getTime();
        cal.add(Calendar.MONTH, -1);// 月份减1
        Date startTime = cal.getTime();
        List<Tenant> tenants = tenantService.getListByPage();
        for(int i=0;i<tenants.size();i++){
            List<App> apps = appService.getAppsByTenantId(tenants.get(i).getId());
            for(int j=0;j<apps.size();j++){
                App app = apps.get(j);
                if(app == null || !App.PRODUCT_CALL_CENTER.equals(app.getServiceType())){
                    continue;
                }
                long sum =  sumAgentNum(tenants.get(i).getId(),app.getId(),startTime,endTime);
                BigDecimal cost = calCostService.calCost(ProductCode.call_center_month.name(),tenants.get(i).getId());
                if(cost != null && cost.compareTo(BigDecimal.ZERO) == 1){
                    Consume consume = new Consume(new Date(), ConsumeCode.call_center_month.name(),cost.multiply(new BigDecimal(sum)),"应用id["+app.getId()+"]总共有"+sum+"个坐席",app.getId(),tenants.get(i).getId(),null);
                    consumeService.consume(consume);
                }
            }
        }

    }
    public long sumAgentNum(String tenantId, String appId, Date startTime, Date endTime) {
        String sql = " select count(1) from (select DISTINCT subaccount_id,name  from db_lsxy_bi_yunhuni.tb_bi_call_center_agent_action_log where tenant_id=? and app_id=? and  action=1 and create_time BETWEEN ? and ? ) a" ;
        return jdbcTemplate.queryForObject(sql,Long.class,tenantId,appId,startTime,endTime);
    }

}
