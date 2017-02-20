package com.lsxy.yunhuni.apicertificate.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificateSubAccount;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuota;
import com.lsxy.yunhuni.api.apicertificate.model.CertAccountQuotaType;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateSubAccountService;
import com.lsxy.yunhuni.api.apicertificate.service.CertAccountQuotaService;
import com.lsxy.yunhuni.api.session.service.VoiceCdrService;
import com.lsxy.yunhuni.apicertificate.dao.CertAccountQuotaDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by liups on 2017/2/15.
 */
@Service
public class CertAccountQuotaServiceImpl extends AbstractService<CertAccountQuota> implements CertAccountQuotaService {
    @Autowired
    CertAccountQuotaDao certAccountQuotaDao;
    @Autowired
    RedisCacheService redisCacheService;
    @Autowired
    ApiCertificateSubAccountService apiCertificateSubAccountService;
    @Autowired
    VoiceCdrService voiceCdrService;
    @Override
    public BaseDaoInterface<CertAccountQuota, Serializable> getDao() {
        return certAccountQuotaDao;
    }

    @Override
    public void updateQuotas(String certAccountId, List<CertAccountQuota> quotas) {
        if(quotas != null){
            for(CertAccountQuota quota : quotas){
                certAccountQuotaDao.updateQuota(certAccountId,quota.getType(),quota.getValue());
            }
        }
    }

    @Override
    public List<CertAccountQuota> findByCertAccountId(String id) {
        return certAccountQuotaDao.findByCertAccountId(id);
    }

    @Override
    public List<CertAccountQuota> findByAppId(String appId) {
        return certAccountQuotaDao.findByCertAccountId(appId);
    }

    @Override
    public void incQuotaUsed(String certAccountId, Date date, Long incV, String type){
        if(incV == null){
            return;
        }
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String key = QUOTA_DAY_PREFIX + "_" + certAccountId + "_" + dateStr;
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Long result = hashOps.increment(type, incV);
        if(result == incV){
            redisCacheService.expire(key,3 * 24 * 60 * 60);
        }
    }

    @Override
    public CertAccountQuota getQuotaRemain(String certAccountId,String type) {
        CertAccountQuota quota = certAccountQuotaDao.findByCertAccountIdAndType(certAccountId,type);
        Long dateUsed = this.getQuotaUsed(quota);
        Long balanceUsed = quota.getUsed() == null ? 0L : quota.getUsed();
        quota.setCurrentUsed(balanceUsed + dateUsed);
        return quota;
    }

    /**
     * 获取配额使用量
     * @param quota
     * @return
     */
    @Override
    public Long getQuotaUsed(CertAccountQuota quota) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date balanceDate = quota.getBalanceDt();
        String balanceDateStr = DateUtils.formatDate(balanceDate, "yyyyMMdd");
        balanceDate = DateUtils.parseDate(balanceDateStr,"yyyyMMdd");
        Long used = quota.getUsed() == null? 0L:quota.getUsed();
        return getQuotaUsed(quota.getCertAccountId(),date,balanceDate,used,quota.getType());
    }

    public Long getQuotaUsed(String certAccountId,Date date,Date lastBalanceDate,Long used,String type){
        if(date.getTime() > lastBalanceDate.getTime()){
            Date newBalanceDate = DateUtils.nextDate(lastBalanceDate);
            Long dateUsed = getQuotaUsedFromRedis(certAccountId, date,type);
            used = used + dateUsed;
            return getQuotaUsed(certAccountId, date, newBalanceDate, used,type);
        }else{
            return used;
        }
    }

    public Long getQuotaUsedFromRedis(String certAccountId,Date date,String type){
        return getIncrLong(certAccountId,date, type);
    }


    /**
     * 从redis中获取增量（购买或消费）
     * @param certAccountId
     * @param date
     * @param type 类型（配额类型）
     * @return 使用时长（秒）
     */
    private Long getIncrLong(String certAccountId, Date date,String type){
        Long used;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String key = QUOTA_DAY_PREFIX + "_" + certAccountId + "_" + dateStr;
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Object incrLongObj = hashOps.get(type);
        String incrLongStr = incrLongObj == null?null:incrLongObj.toString();
        if(StringUtils.isBlank(incrLongStr)){
            used = 0L;
        }else{
            used = Long.parseLong(incrLongStr);
        }
        return used;
    }

    @Override
    public void dayStatics(Date date) {
        String yyyyMMdd = DateUtils.formatDate(date, "yyyyMMdd");
        Date staticsDate = DateUtils.parseDate(yyyyMMdd,"yyyyMMdd");
        Iterable<ApiCertificateSubAccount> subaccounts = apiCertificateSubAccountService.list();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future> results = new ArrayList<>();
        for(ApiCertificateSubAccount subaccount:subaccounts){
            results.add(executorService.submit(() -> this.staticSubaccountQuota(staticsDate, subaccount)));
        }
        for(Future f : results){
            try {
                f.get();
            }catch (Throwable t){

            }
        }
        executorService.shutdown();
    }

    private void staticSubaccountQuota(Date staticsDate, ApiCertificateSubAccount subaccount) {
        List<CertAccountQuota> quotas = this.findByCertAccountId(subaccount.getId());
        Date endDate = DateUtils.nextDate(staticsDate);
        for(CertAccountQuota quota : quotas){
            CertAccountQuotaType type = CertAccountQuotaType.valueOf(quota.getType());
            Date balanceDt = quota.getBalanceDt();
            String balanceDateStr = DateUtils.formatDate(balanceDt, "yyyyMMdd");
            balanceDt = DateUtils.parseDate(balanceDateStr,"yyyyMMdd");
            Date startDate = DateUtils.nextDate(balanceDt);
            switch (type){
                case CallQuota:{
                    Map staticCdr = voiceCdrService.getStaticCdr(null, null, subaccount.getId(), startDate, endDate);
                    Long costTimeLong = (Long) staticCdr.get("costTimeLong");
                    Long oleUsed = quota.getUsed() == null ? 0L : quota.getUsed();
                    quota.setUsed(oleUsed + costTimeLong);
                    this.save(quota);
                }
            }
        }
    }

}
