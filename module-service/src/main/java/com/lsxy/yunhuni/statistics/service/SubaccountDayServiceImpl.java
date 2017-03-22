package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.statistics.model.SubaccountDay;
import com.lsxy.yunhuni.api.statistics.model.SubaccountStatisticalVO;
import com.lsxy.yunhuni.api.statistics.service.SubaccountDayService;
import com.lsxy.yunhuni.statistics.dao.SubaccountDayDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class SubaccountDayServiceImpl extends AbstractService<SubaccountDay> implements SubaccountDayService {
    @PersistenceContext
    private EntityManager em;
    @Autowired
    SubaccountDayDao subaccountDayDao;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<SubaccountDay, Serializable> getDao() {
        return this.subaccountDayDao;
    }

    @Override
    public Page<SubaccountStatisticalVO> getPageByConditions(Integer pageNo, Integer pageSize, Date startTime, Date endTime, String tenantId, String appId, String subaccountId) {
        String sql = " FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day obj " +
                "LEFT JOIN db_lsxy_bi_yunhuni.tb_bi_app a ON obj.app_id=a.id " +
                "LEFT JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert s ON obj.subaccount_id=s.id " +
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
                "s.cert_id as cert_id," +
                "s.secret_key as secret_key," +
                "obj.app_id as app_id," +
                "a.name as app_name," +
                "obj.among_amount as among_amount," +
                "ROUND(obj.among_duration/60,0) as among_duration," +
                "concat( (CASE WHEN obj.voice_used IS NULL THEN '0'ELSE ROUND(obj.voice_used/60,0) END) ,'/', (CASE WHEN obj.voice_quota_value IS NULL THEN '0' WHEN obj.voice_quota_value<0 THEN '∞' ELSE ROUND(obj.voice_quota_value/60,0) END) ) as voice_num," +
                "concat( (CASE WHEN obj.msg_used IS NULL THEN '0'ELSE obj.msg_used END)  ,'/', (CASE WHEN obj.msg_quota_value IS NULL THEN '0' WHEN obj.msg_quota_value<0 THEN '∞' ELSE obj.msg_quota_value END)) as seat_num ," +
                "concat( (CASE WHEN obj.ussd_used IS NULL THEN '0'ELSE obj.ussd_used END)  ,'/', (CASE WHEN obj.ussd_quota_value IS NULL THEN '0' WHEN obj.ussd_quota_value<0 THEN '∞' ELSE obj.ussd_quota_value END)) as ussd_num ," +
                "concat( (CASE WHEN obj.sms_used IS NULL THEN '0'ELSE obj.sms_used END)  ,'/', (CASE WHEN obj.sms_quota_value IS NULL THEN '0' WHEN obj.sms_quota_value<0 THEN '∞' ELSE obj.sms_quota_value END)) as sms_num ," +
                "sum( (CASE WHEN obj.sms_used IS NULL THEN '0'ELSE obj.sms_used END) + (CASE WHEN obj.ussd_used IS NULL THEN '0'ELSE obj.ussd_used END) ) as among_msg  "+sql;
        Query countQuery = em.createNativeQuery(countSql);
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
        return new Page<>(start+1,total,pageSize,list);
    }

    @Override
    public List<SubaccountStatisticalVO> getListByConditions(Date startTime, Date endTime, String tenantId, String appId, String subaccountId) {
        int pageNo = 1;
        int pageSize = 100;
        List list = new ArrayList();
        Page<SubaccountStatisticalVO> page = getPageByConditions(pageNo,pageSize,startTime,  endTime,  tenantId,  appId,  subaccountId);
        if(page.getResult()!=null) {
            list.addAll(page.getResult());
            while (page.getCurrentPageNo() < page.getTotalPageCount()) {
                pageNo = Long.valueOf(page.getCurrentPageNo()).intValue() + 1;
                page = getPageByConditions(pageNo, pageSize, startTime, endTime, tenantId, appId, subaccountId);
                if(page.getResult()!=null) {
                    list.addAll(page.getResult());
                }else{
                    break;
                }
            }
        }
        return list;
    }

    @Override
    public Map sum(Date start, Date end, String tenantId, String appId, String subaccountId) {
        //amongAmount
        //amongDuration
        String sql = " select  IFNULL(sum(among_amount),0) as amongAmount, IFNULL(sum(among_duration),0) as amongDuration from db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day where deleted=0 ";
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
    public void dayStatistics(Date date){
        String dd = DateUtils.formatDate(date, "dd");
        int day = Integer.parseInt(dd);
        String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
        Date staticsDate = DateUtils.parseDate(dateStr, "yyyy-MM-dd");
        String statisticsDateStr = DateUtils.formatDate(staticsDate);
        Date preDate = DateUtils.getPreDate(staticsDate);
        String preDateStr = DateUtils.formatDate(preDate);
        Date nextDate = DateUtils.nextDate(staticsDate);
        String nextDateStr = DateUtils.formatDate(nextDate);
        String currentDateStr = DateUtils.formatDate(new Date());

        //子账号最初开始有的日期2017-02-22，之前的日期不用算了
        Date firstStatisticsDate = DateUtils.parseDate("2017-02-22", "yyyy-MM-dd");
        SubaccountDay lastStatistics = subaccountDayDao.findFirstByDt(preDate);

        //如果前一天没有统计数据，并且要统计的时间大于2017-02-22,则先统计前一天的数据
        if(lastStatistics == null && staticsDate.getTime() > firstStatisticsDate.getTime()) {
            dayStatistics(preDate);
        }
        //如果今天有统计数据了，则说明不用统计了
        SubaccountDay todayStatistics = subaccountDayDao.findFirstByDt(staticsDate);
        if(todayStatistics != null){
            return;
        }

        String sql = "SELECT REPLACE(UUID(), '-', '') AS id,a.app_id,a.tenant_id,a.id AS subaccount_id ,'"+statisticsDateStr+"' AS dt, "+ day +" AS day , IFNULL(c.among_amount,0) AS among_amount, IFNULL(b.among_duration,0) AS among_duration," +
                "IFNULL((SELECT d.voice_used FROM db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day d WHERE d.subaccount_id = a.id AND d.dt = '" + preDateStr + "'),0) + IFNULL(b.among_duration,0) AS voice_used, 0 AS msg_used ," +
                "IFNULL((SELECT qu.value FROM db_lsxy_bi_yunhuni.tb_bi_cert_account_quota qu WHERE qu.type='CallQuota' AND qu.cert_account_id = a.id LIMIT 1),0) AS voice_quota_value, -1 AS msg_quota_value ," +
                "'"+currentDateStr + "' AS create_time, '"+ currentDateStr + "' AS last_time," + " 0 AS deleted,0 AS sortno,0 AS version "+
                "FROM (SELECT p.tenant_id ,s.app_id ,p.id FROM db_lsxy_bi_yunhuni.tb_bi_api_cert p INNER JOIN db_lsxy_bi_yunhuni.tb_bi_api_cert_subaccount s ON p.id = s.id WHERE (p.deleted = 0 OR (p.deleted = 1 AND p.delete_time > '" + statisticsDateStr + "')) AND p.create_time < '" + nextDateStr +"') a " +
                "LEFT JOIN " +
                "(SELECT tenant_id,app_id,subaccount_id,SUM(cost_time_long) AS among_duration  " +
                "FROM db_lsxy_bi_yunhuni.tb_bi_voice_cdr WHERE call_end_dt >= '" + statisticsDateStr + "'  AND call_end_dt < '" + nextDateStr + "' GROUP BY tenant_id,app_id,subaccount_id) b " +
                "ON a.id = b.subaccount_id AND a.tenant_id = b.tenant_id AND a.app_id = b.app_id " +
                "LEFT JOIN " +
                "(SELECT tenant_id,app_id,subaccount_id,SUM(amount) AS among_amount " +
                "FROM db_lsxy_bi_yunhuni.tb_bi_consume WHERE dt >= '" + statisticsDateStr + "' AND dt < '" + nextDateStr + "' GROUP BY tenant_id,app_id,subaccount_id) c " +
                "ON a.id = c.subaccount_id AND a.tenant_id = c.tenant_id AND a.app_id = c.app_id " ;

        Query query = getEm().createNativeQuery(sql);
        List result = query.getResultList();
        if(result != null && result.size() >0){

            String insertSql = "INSERT INTO db_lsxy_bi_yunhuni.tb_bi_cert_subaccount_day (id,app_id,tenant_id,subaccount_id,dt,day,among_amount,among_duration,voice_used," +
                    "msg_used,voice_quota_value,msg_quota_value,create_time,last_time,deleted,sortno,version) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

            jdbcTemplate.batchUpdate(insertSql,result);
        }
    }



}
