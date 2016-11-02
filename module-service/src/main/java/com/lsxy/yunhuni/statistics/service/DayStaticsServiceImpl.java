package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;
import com.lsxy.yunhuni.api.statistics.service.DayStaticsService;
import com.lsxy.yunhuni.statistics.dao.DayStaticsDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.lsxy.framework.core.utils.DateUtils.nextDate;

/**
 * Created by liups on 2016/10/21.
 */
@Service
public class DayStaticsServiceImpl extends AbstractService<DayStatics> implements DayStaticsService {
    @Autowired
    DayStaticsDao dayStaticsDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    RechargeService rechargeService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    VoiceCdrService voiceCdrService;
    @Autowired
    AppService appService;

    @Override
    public BaseDaoInterface<DayStatics, Serializable> getDao() {
        return this.dayStaticsDao;
    }

    @Override
    public void dayStatics(Date date) {
        String yyyyMMdd = DateUtils.formatDate(date, "yyyyMMdd");
        Date staticsDate = DateUtils.parseDate(yyyyMMdd,"yyyyMMdd");
        Iterable<Tenant> tenants = tenantService.list();
        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> results = new ArrayList<>();
        for(Tenant tenant:tenants){
            results.add(executorService.submit(() -> this.staticTenantAndApp(staticsDate, tenant)));
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
    public void staticTenantAndApp(Date staticsDate, Tenant tenant) {
        DayStatics tenantDayStatics = dayStaticsDao.findFirstByTenantIdAndAppIdIsNullOrderByDtDesc(tenant.getId());
        if(tenantDayStatics != null){
            tenantDayStatics(tenantDayStatics,staticsDate);
        }else{
            Date createTime = tenant.getCreateTime();
            Date preCreateDate = DateUtils.getPreDate(createTime);
            String preCreateDateStr = DateUtils.formatDate(preCreateDate, "yyyyMMdd");
            Date dt = DateUtils.parseDate(preCreateDateStr,"yyyyMMdd");
            tenantDayStatics = new DayStatics(tenant.getId(),null,dt, BigDecimal.ZERO,BigDecimal.ZERO,0L,0L,0L);
            tenantDayStatics(tenantDayStatics,staticsDate);
        }
        List<App> apps = appService.getAppsByTenantId(tenant.getId());
        for(App app:apps){
            DayStatics appDayStatics = dayStaticsDao.findFirstByAppIdOrderByDtDesc(app.getId());
            if(appDayStatics != null){
                appDayStatics(appDayStatics,staticsDate);
            }else{
                Date appCreateTime = app.getCreateTime();
                Date preAppCreateTime = DateUtils.getPreDate(appCreateTime);
                String preAppCreateTimeStr = DateUtils.formatDate(preAppCreateTime, "yyyyMMdd");
                Date appDt = DateUtils.parseDate(preAppCreateTimeStr,"yyyyMMdd");
                appDayStatics = new DayStatics(tenant.getId(),app.getId(),appDt,BigDecimal.ZERO,BigDecimal.ZERO,0L,0L,0L);
                appDayStatics(appDayStatics,staticsDate);
            }
        }
    }


    private void tenantDayStatics(DayStatics lastDayStatics,Date date){
        Date lastStaticsDate = lastDayStatics.getDt();
        String tenantId = lastDayStatics.getTenantId();
        if(lastStaticsDate.getTime() < date.getTime()){
            Date startDate = nextDate(lastStaticsDate);
            Date endDate = DateUtils.nextDate(startDate);
            BigDecimal recharge = rechargeService.getRechargeByTenantIdAndDate(tenantId,startDate,endDate);
            BigDecimal consume = consumeService.getConsumeByTenantIdAndDate(tenantId,startDate,endDate);
            Map staticMap = voiceCdrService.getStaticCdr(tenantId, null, startDate, endDate);
            Long callSum = (Long) staticMap.get("callSum");
            BigDecimal askSum = (BigDecimal) staticMap.get("askSum");
            BigDecimal costTimeLong = (BigDecimal) staticMap.get("costTimeLong");

            DayStatics current = new DayStatics(tenantId,null,startDate,
                    lastDayStatics.getRecharge().add(recharge),
                    lastDayStatics.getConsume().add(consume),
                    lastDayStatics.getCallConnect() + askSum.longValue(),
                    lastDayStatics.getCallSum() + callSum,
                    lastDayStatics.getCallCostTime() + costTimeLong.longValue()
                    );
            this.save(current);
            tenantDayStatics(current,date);
        }
    }

    private void appDayStatics(DayStatics lastDayStatics,Date date){
        Date lastStaticsDate = lastDayStatics.getDt();
        String tenantId = lastDayStatics.getTenantId();
        String appId = lastDayStatics.getAppId();
        if(lastStaticsDate.getTime() < date.getTime()){
            Date startDate = nextDate(lastStaticsDate);
            Date endDate = DateUtils.nextDate(startDate);
            BigDecimal consume = consumeService.getConsumeByAppIdAndDate(appId,startDate,endDate);
            Map staticMap = voiceCdrService.getStaticCdr(null, appId, startDate, endDate);
            Long callSum = (Long) staticMap.get("callSum");
            BigDecimal askSum = (BigDecimal) staticMap.get("askSum");
            BigDecimal costTimeLong = (BigDecimal) staticMap.get("costTimeLong");

            DayStatics current = new DayStatics(tenantId,appId,startDate,
                    BigDecimal.ZERO,
                    lastDayStatics.getConsume().add(consume),
                    lastDayStatics.getCallConnect() + askSum.longValue(),
                    lastDayStatics.getCallSum() + callSum.longValue(),
                    costTimeLong.longValue() + lastDayStatics.getCallConnect()
            );
            this.save(current);
            appDayStatics(current,date);
        }
    }

    @Override
    public DayStatics getStaticByTenantId(String tenantId,Date date) {
//        Date date = DateUtils.getPreDate(new Date());
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        Date dt = DateUtils.parseDate(dateStr,"yyyyMMdd");
        DayStatics dayStatics = dayStaticsDao.findFirstByTenantIdAndDtAndAppIdIsNull(tenantId,dt);
        if(dayStatics==null){
            dayStatics = dayStaticsDao.findFirstByTenantIdAndDtLessThanAndAppIdIsNullOrderByDtDesc(tenantId,dt);
        }
        return dayStatics;
    }

}
