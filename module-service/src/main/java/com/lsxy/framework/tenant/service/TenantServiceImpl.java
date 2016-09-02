package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.*;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.tenant.dao.RealnameCorpDao;
import com.lsxy.framework.tenant.dao.RealnamePrivateDao;
import com.lsxy.framework.tenant.dao.TenantDao;
import com.lsxy.yunhuni.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.file.model.VoiceFilePlay;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

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
    @Autowired
    private CalBillingService calBillingService;

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
    public Tenant createTenant(Account account) {
        long incTid = cacheManager.incr(INCREASE_TID);
        Tenant tenant = new Tenant();
        tenant.setTenantName(account.getUserName());
        tenant.setRegisterUserId(account.getId());
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

    @Override
    public Page<TenantVO> pageListBySearch(String name,Date regDateStart, Date regDateEnd,
                                           Integer authStatus, Integer accStatus, int pageNo, int pageSize) {
        String sql =" FROM db_lsxy_base.tb_base_tenant t" +
                " LEFT JOIN (SELECT b.tenant_id,b.`status` FROM db_lsxy_base.tb_base_account b GROUP BY b.tenant_id) a ON t.id = a.tenant_id" +
                " LEFT JOIN (SELECT tenant_id,count(id) s FROM db_lsxy_bi_yunhuni.tb_bi_app where deleted <> 1 GROUP BY tenant_id) app ON t.id = app.tenant_id" +
                " LEFT JOIN db_lsxy_bi_yunhuni.tb_bi_billing billing ON t.id = billing.tenant_id" +
                " LEFT JOIN (SELECT tenant_id,sum(among_amount) as sum_amount FROM db_lsxy_base.tb_base_consume_day WHERE app_id IS NULL AND tenant_id IS NOT NULL AND type IS NULL GROUP BY tenant_id) consume ON t.id = consume.tenant_id" +
                " LEFT JOIN (SELECT tenant_id,sum(amount) amount FROM tb_base_recharge WHERE `status` = 'PAID' GROUP BY tenant_id ) recharge on t.id = recharge.tenant_id" +
                " LEFT JOIN (SELECT tenant_id,sum(among_call) as sum_call,sum(among_duration) as sum_duration  FROM db_lsxy_base.tb_base_voice_cdr_day WHERE app_id IS NULL AND tenant_id IS NOT NULL AND type IS NULL ORDER BY tenant_id) cdr ON t.id = cdr.tenant_id" +
                " WHERE 1=1";
        sql += " AND (a.`status` IN ("+Account.STATUS_NORMAL+","+Account.STATUS_LOCK+"))";
        if(StringUtil.isNotEmpty(name)){
           sql += " AND (t.tenant_name LIKE :name)";
        }
        if(regDateStart !=null){
            sql += " AND (t.create_time >= :start)";
        }
        if(regDateEnd !=null){
            sql += " AND (t.create_time <= :end)";
        }
        if(accStatus !=null){
            sql += " AND (a.`status` = :accStatus)";
        }
        if(authStatus!=null){//认证状态
            if(authStatus == 1){//已认证
                sql += " AND (t.is_real_auth IN ("+ StringUtils.join(Tenant.AUTH_STATUS,",") + "))";
            }
            if(authStatus == 0){
                sql += " AND (t.is_real_auth NOT IN (" + StringUtils.join(Tenant.AUTH_STATUS,",") + "))";
            }
        }
        String countSql = "SELECT COUNT(t.id) " + sql;
        String pageSql = "SELECT t.id 'id',t.tenant_name 'name'," +
                "t.create_time 'regDate',t.is_real_auth 'authStatus'," +
                "a.`status` 'accountStatus',app.s 'appCount',billing.balance 'remainCoin'," +
                "consume.sum_amount 'costCoin',recharge.amount 'totalCoin'," +
                "cdr.sum_call 'sessionCount',cdr.sum_duration 'sessionTime'" + sql;
        Query countQuery = em.createNativeQuery(countSql);
        Query pageQuery = em.createNativeQuery(pageSql,"tenantResult");
        if(StringUtil.isNotEmpty(name)){
            countQuery.setParameter("name","%"+name+"%");
            pageQuery.setParameter("name","%"+name+"%");
        }
        if(regDateStart !=null){
            countQuery.setParameter("start",regDateStart);
            pageQuery.setParameter("start",regDateStart);
        }
        if(regDateEnd !=null){
            countQuery.setParameter("end",regDateEnd);
            pageQuery.setParameter("end",regDateEnd);
        }
        if(accStatus !=null){
            countQuery.setParameter("accStatus",accStatus);
            pageQuery.setParameter("accStatus",accStatus);
        }

        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        //修改余额取值
        List<TenantVO> temp = pageQuery.getResultList();
        List<TenantVO> list = new ArrayList();
        for(int i=0;i<temp.size();i++){
            TenantVO tenantVO = temp.get(i);
            BigDecimal bigDecimal =  calBillingService.getBalance(tenantVO.getId());
            tenantVO.setRemainCoin(bigDecimal.doubleValue());
            list.add(tenantVO);
        }
        return new Page<>(start,total,pageSize,list);
    }
    @Override
    public List<Tenant> pageListByUserName(String name) {
        String hql = " FROM Tenant obj WHERE obj.tenantName like ?1 ";
        List<Tenant> list = this.list(hql,"%"+name+"%");
        return list;
    }
    @Override
    public Map getAwaitNum() {
        String hql = " from Tenant obj where obj.isRealAuth=?1 or obj.isRealAuth=?2 ";
        long tenant =  this.countByCustom(hql,Tenant.AUTH_WAIT,Tenant.AUTH_ONESELF_WAIT);
        String hql2 = "  from VoiceFilePlay obj where obj.status=?1  ";
        long voiceFilePlay = this.countByCustom(hql2, VoiceFilePlay.STATUS_WAIT);
        Map map = new HashMap();
        map.put("tenant",tenant);
        map.put("voiceFilePlay",voiceFilePlay);
        return map;
    }
}
