package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.RealnameCorp;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.tenant.dao.RealnameCorpDao;
import com.lsxy.framework.tenant.dao.RealnamePrivateDao;
import com.lsxy.framework.tenant.dao.TenantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class TenantServiceImpl extends AbstractService<Tenant> implements TenantService {
    private static final String INCREASE_TID = "increase_tid";  //保存在redis里的自动增长的租户各识别码，达到9999后重新归0重新计

    @Autowired
    private AccountService accountService;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private RealnameCorpDao realnameCorpDao;

    @Autowired
    private RealnamePrivateDao realnamePrivateDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RedisCacheService cacheManager;

    @Override
    public BaseDaoInterface<Tenant, Serializable> getDao() {
        return this.tenantDao;
    }

    @Override
    public Tenant findTenantByUserName(String userName)  {
        Tenant tenant = null;
        Account account = accountService.findAccountByUserName(userName);
        if(account != null){
            tenant = account.getTenant();
        }
        return tenant;
    }

    @Override
    public Tenant createTenant() {
        long incTid = cacheManager.incr(INCREASE_TID);
        Tenant tenant = new Tenant();
        tenant.setIsRealAuth(Tenant.AUTH_NO); //设为未实名认证状态
        tenant.setTenantUid(DateUtils.getTime("yyyyMMdd")+ incTid);
        if(incTid >= 9999){
            cacheManager.del(INCREASE_TID);
        }
        return this.save(tenant);
    }

    @Override
    public int countValidTenant() {
        return tenantDao.countByDeleted(Boolean.FALSE);
    }

    public int countValidTenantDateBetween(Date d1, Date d2){
        if(d1 == null){
            throw new NullPointerException();
        }
        if(d2 == null){
            throw new NullPointerException();
        }
        return tenantDao.countByDeletedAndCreateTimeBetween(Boolean.FALSE,d1,d2);
    }

    @Override
    public int countValidTenantToday() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(today);
        Date d2 = DateUtils.setEndDay(today);
        return countValidTenantDateBetween(d1,d2);
    }

    @Override
    public int countValidTenantWeek() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstDayOfWeek(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastDayOfWeek(today));
        return countValidTenantDateBetween(d1,d2);
    }

    @Override
    public int countValidTenantMonth() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstTimeOfMonth(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastTimeOfMonth(today));
        return countValidTenantDateBetween(d1,d2);
    }

    @Override
    public int countAuthTenant() {
        return tenantDao.countByIsRealAuthIn(Tenant.AUTH_STATUS);
    }

    /**
     * 认证数量=uniq(个人认证+ 企业认证)
     * @param d1 开始日期
     * @param d2 结束日期
     * @return
     */
    public int countAuthTenantDateBetween(Date d1,Date d2){
        List<RealnameCorp> corps = realnameCorpDao.findByStatusAndCreateTimeBetween(Tenant.AUTH_COMPANY_SUCCESS,d1,d2);
        List<RealnamePrivate> privates = realnamePrivateDao.findByStatusAndCreateTimeBetween(Tenant.AUTH_ONESELF_SUCCESS,d1,d2);
        Set<String> results = new HashSet<String>();
        for (RealnameCorp corp : corps) {
            if(corp!=null && corp.getTenant()!=null){
                results.add(corp.getTenant().getId());
            }
        }
        for (RealnamePrivate p : privates) {
            if(p!=null && p.getTenant()!=null){
                results.add(p.getTenant().getId());
            }
        }
        return results.size();
    }

    @Override
    public int countAuthTenantToday() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(today);
        Date d2 = DateUtils.setEndDay(today);
        return countAuthTenantDateBetween(d1,d2);
    }

    @Override
    public int countAuthTenantWeek() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstDayOfWeek(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastDayOfWeek(today));
        return countAuthTenantDateBetween(d1,d2);
    }

    @Override
    public int countAuthTenantMonth() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstTimeOfMonth(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastTimeOfMonth(today));
        return countAuthTenantDateBetween(d1,d2);
    }

    @Override
    public int countConsumeTenant() {
        String countSQL = "SELECT count(tenant_id) as c FROM " +
                "(SELECT tenant_id FROM tb_base_consume_day WHERE deleted=0 AND tenant_id IS NOT NULL GROUP BY tenant_id) a";
        Query queryCount = em.createNativeQuery(countSQL);
        return ((BigInteger) queryCount.getSingleResult()).intValue();
    }

    public int countConsumeTenantDateBetween(Date d1,Date d2){
        String countSQL = "SELECT count(tenant_id) as c FROM " +
                "(SELECT tenant_id FROM tb_base_consume_day WHERE deleted=0 AND tenant_id IS NOT NULL AND dt between :d1 and :d2 GROUP BY tenant_id) a";
        Query queryCount = em.createNativeQuery(countSQL);
        queryCount.setParameter("d1", d1);
        queryCount.setParameter("d2", d2);
        return ((BigInteger) queryCount.getSingleResult()).intValue();
    }
    @Override
    public int countConsumeTenantToday() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(today);
        Date d2 = DateUtils.setEndDay(today);
        return countConsumeTenantDateBetween(d1,d2);
    }

    @Override
    public int countConsumeTenantWeek() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstDayOfWeek(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastDayOfWeek(today));
        return countConsumeTenantDateBetween(d1,d2);
    }

    @Override
    public int countConsumeTenantMonth() {
        Date today = new Date();
        Date d1 = DateUtils.setStartDay(DateUtils.getFirstTimeOfMonth(today));
        Date d2 = DateUtils.setEndDay(DateUtils.getLastTimeOfMonth(today));
        return countConsumeTenantDateBetween(d1,d2);
    }
}
