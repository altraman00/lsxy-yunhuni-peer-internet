package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.statistics.model.SubaccountMonth;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;
import com.lsxy.yunhuni.api.statistics.service.SubaccountMonthService;
import com.lsxy.yunhuni.statistics.dao.SubaccountMonthDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liups on 2017/2/21.
 */
@Service
public class SubaccountMonthServiceImpl extends AbstractService<SubaccountMonth> implements SubaccountMonthService {
    @Autowired
    SubaccountMonthDao subaccountMonthDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @PersistenceContext
    private EntityManager em;
    @Override
    public BaseDaoInterface<SubaccountMonth, Serializable> getDao() {
        return this.subaccountMonthDao;
    }

    @Override
    public Page<SubaccountStatisticalVO> getPageByConditions(Integer pageNo, Integer pageSize, Date startTime, Date endTime, String tenantId,  String appId,String subaccountId) {
        String sql = " FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_month obj " +
                "LEFT JOIN db_lsxy_bi_yunhuni.tb_bi_app a ON obj.app_id=a.id " +
                "LEFT JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert s ON obj.subaccount_id=s.cert_id " +
                "WHERE obj.deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql += " AND app_id = :appId";
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            sql += " AND obj.subaccount_id = :subaccountId ";
        }
        sql += " AND obj.tenant_id =:tenantId AND obj.dt BETWEEN :startTime AND :endTime ";
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT obj.id as id," +
                "obj.subaccount_id as certId," +
                "s.secret_key as secretKey," +
                "obj.app_id as appId," +
                "a.name as appName," +
                "obj.among_amount as amongAmount," +
                "obj.among_duration as amongDuration," +
                "(CASE WHEN obj.voice_used IS NULL THEN '0'ELSE obj.voice_used END) as test1 ," +
                "concat( (CASE WHEN obj.voice_used IS NULL THEN '0'ELSE obj.voice_used END) ,'/', (CASE WHEN obj.voice_quota_value IS NULL THEN '0' WHEN obj.voice_quota_value<0 THEN '∞' ELSE obj.voice_quota_value END) ) as voiceNum," +
                "concat( (CASE WHEN obj.msg_used IS NULL THEN '0'ELSE obj.msg_used END)  ,'/', (CASE WHEN obj.msg_quota_value IS NULL THEN '0' WHEN obj.msg_quota_value<0 THEN '∞' ELSE obj.msg_quota_value END)) as seatNum "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" group by obj.dt desc";
        Query pageQuery = em.createNativeQuery(pageSql,SubaccountStatisticalVO.class);
        if(StringUtils.isNotEmpty(appId)){
            countQuery.setParameter("appId",appId);
            pageQuery.setParameter("appId",appId);
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            countQuery.setParameter("subaccountId",subaccountId);
            pageQuery.setParameter("subaccountId",subaccountId);
        }
        countQuery.setParameter("tenantId",tenantId);
        pageQuery.setParameter("tenantId",tenantId);
        countQuery.setParameter("startTime",startTime);
        pageQuery.setParameter("startTime",startTime);
        countQuery.setParameter("endTime",endTime);
        pageQuery.setParameter("endTime",endTime);
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return new Page<>(start,total,pageSize,null);
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return new Page<>(start,total,pageSize,list);
    }

    @Override
    public List<SubaccountStatisticalVO> getListByConditions(Date startTime, Date endTime, String tenantId, String appId, String subaccountId) {
        int pageNo = 1;
        int pageSize = 100;
        List list = new ArrayList();
        Page<SubaccountStatisticalVO> page = getPageByConditions(pageNo,pageSize,startTime,  endTime,  tenantId,  appId,  subaccountId);
        list.addAll(page.getResult());
        if(page.getCurrentPageNo() < page.getTotalPageCount()){
            pageNo = Long.valueOf(page.getCurrentPageNo()).intValue()+1;
            page = getPageByConditions(pageNo,pageSize,startTime,  endTime,  tenantId,  appId,  subaccountId);
            list.addAll(page.getResult());
        }
        return list;
    }

    @Override
    public Map sum(Date start, Date end, String tenantId, String appId, String subaccountId) {
        //amongAmount
        //amongDuration
        String sql = " select sum(among_amount) as amongAmount,sum(among_duration) as amongDuration from db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_month where deleted=0 ";
        if(StringUtils.isNotEmpty(appId)){
            sql += " AND app_id = '"+appId+"'";
        }
        if(StringUtils.isNotEmpty(subaccountId)){
            sql += " AND subaccount_id = '"+subaccountId+"'";
        }
        sql += " AND tenant_id =? AND dt BETWEEN ? AND ? ";
        return jdbcTemplate.queryForMap(sql,tenantId,start,end);
    }


    @Override
    public void monthStatistics(Date date){
        String m = DateUtils.formatDate(date, "MM");
        int month = Integer.parseInt(m);
        String monthStr = DateUtils.formatDate(date, "yyyy-MM");
        Date staticsDate = DateUtils.parseDate(monthStr, "yyyy-MM");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPrevMonth(staticsDate);
        String preDateStr = DateUtils.formatDate(preDate);
        String nextDateStr = DateUtils.getNextMonth(statisticsDateStr, "yyyy-MM-dd HH:mm:ss");

        String sql = " SELECT a.tenant_id,a.app_id,a.id AS subaccount_id , IFNULL(b.among_duration,0) AS among_duration, IFNULL(b.among_amount,0) AS among_amount, '"+statisticsDateStr+"' AS dt, "+ month +" AS MONTH , 0 AS msg_used ," +
                " IFNULL((SELECT d.voice_used FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day d WHERE d.subaccount_id = a.id AND d.dt = '" + preDateStr + "'),0) + IFNULL(b.among_duration,0) AS voice_used, " +
                " IFNULL((SELECT qu.value FROM db_lsxy_bi_yunhuni.tb_bi_cert_account_quota qu WHERE qu.type='CallQuota' AND qu.cert_account_id = a.id LIMIT 1),0) AS voice_quota_value, -1 AS msg_quota_value " +
                " FROM (SELECT p.tenant_id ,s.app_id ,p.id FROM db_lsxy_bi_yunhuni.tb_bi_api_cert p INNER JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert_subaccount s ON p.id = s.id WHERE p.deleted = 0) a " +
                " LEFT JOIN " +
                " ( SELECT tenant_id,app_id,subaccount_id,SUM(among_duration) AS among_duration,SUM(among_amount) AS among_amount  " +
                " FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day  WHERE dt >= '" + statisticsDateStr + "' AND  dt < '" + nextDateStr + "' GROUP BY tenant_id,app_id,subaccount_id) b " +
                " ON a.id = b.subaccount_id ";
        Query query = getEm().createNativeQuery(sql, SubaccountMonth.class);
        List<SubaccountMonth> result = query.getResultList();
        //TODO 批量保存
        if(result != null){
            for(SubaccountMonth monthStatics : result){
                this.save(monthStatics);
            }
        }
    }

}
