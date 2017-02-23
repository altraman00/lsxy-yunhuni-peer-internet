package com.lsxy.yunhuni.statistics.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ConsumeDay;
import com.lsxy.yunhuni.api.statistics.service.ConsumeDayService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.statistics.dao.ConsumeDayDao;
import com.lsxy.utils.StatisticsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 消费日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeDayServiceImpl extends AbstractService<ConsumeDay> implements ConsumeDayService {
    @Autowired
    ConsumeDayDao consumeDayDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private EntityManager em;

    @Override
    public BaseDaoInterface<ConsumeDay, Serializable> getDao() {
        return consumeDayDao;
    }

    @Override
    public Page<ConsumeDay> pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize) {
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" obj.dt >=?1 and obj.dt<=?2  ORDER BY obj.dt";
        Page<ConsumeDay>   page =  this.pageList(hql,pageNo,pageSize,startTime,endTime);
        return page;
    }

    @Override
    public Page<ConsumeDay> compareStartTimeAndEndTimePageList(String tenantId, String appId, String type, Date startTime1, Date endTime1, Date startTime2, Date endTime2, Integer pageNo, Integer pageSize) {
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+"  (obj.dt BETWEEN ?1 and ?2  or obj.dt  between ?3 and ?4 ) ORDER BY obj.dt";
        Page<ConsumeDay>   page =  this.pageList(hql,pageNo,pageSize,startTime1,endTime1,startTime2,endTime2);
        return page;
    }

    @Override
    public List<ConsumeDay> list(Object tenantId, Object appId,Object type,Date startTime, Date endTime) {
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNotNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.dt";
        List<ConsumeDay>  list = this.list(hql,startTime,endTime);
        return list;
    }

    @Override
    public Long countByTime(String userName, String appId, String startTime, String endTime) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String nextMonth = DateUtils.getNextMonth(endTime, "yyyy-MM");
        Date start = DateUtils.parseDate(startTime,"yyyy-MM");
        Date end = DateUtils.parseDate(nextMonth,"yyyy-MM");
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenant.getId(),appId, null)+" obj.dt>=?1 and obj.dt<?2";
        long count = this.countByCustom(hql, start, end);
        return count;
    }

    @Override
    public List<ConsumeDay> pageListByTime(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String nextMonth = DateUtils.getNextMonth(endTime, "yyyy-MM");
        Date date1 = DateUtils.parseDate(startTime,"yyyy-MM");
        Date date2 = DateUtils.parseDate(nextMonth,"yyyy-MM");
        String sql = " FROM db_lsxy_bi_yunhuni.tb_bi_consume_day obj WHERE "+StatisticsUtils.getNativeSqlIsNull(tenant.getId(),appId, null)+" obj.deleted=0 AND obj.dt>=:date1 AND obj.dt<:date2 ";
        String countSql = " SELECT COUNT(1) "+sql;
        String pageSql = " SELECT * "+sql;
        Query countQuery = em.createNativeQuery(countSql);
        pageSql +=" ORDER BY obj.create_time";
        Query pageQuery = em.createNativeQuery(pageSql,ConsumeDay.class);
        countQuery.setParameter("date1",date1);
        pageQuery.setParameter("date1",date1);
        countQuery.setParameter("date2",date2);
        pageQuery.setParameter("date2",date2);
        int total = ((BigInteger)countQuery.getSingleResult()).intValue();
        int start = (pageNo-1)*pageSize;
        if(total == 0){
            return null;
        }
        pageQuery.setMaxResults(pageSize);
        pageQuery.setFirstResult(start);
        List list = pageQuery.getResultList();
        return list;
    }

    @Override
    public void dayStatistics(Date date1, int day1, Date date2, int day2, String[] select,String[] all) throws SQLException {
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        //拼装sql
        String sql = " SELECT "+selects+"  REPLACE(UUID(), '-', '') as id, ? as dt,? as day, "+
                     " IFNULL(sum(among_amount),0) as among_amount, " +
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                     " from db_lsxy_bi_yunhuni.tb_bi_consume_hour a where tenant_id is not null and app_id is not null and type is not null and dt BETWEEN ? AND ? "+groupbys;
        //拼装参数
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Timestamp sqlDate2 = new Timestamp(date2.getTime());
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd")+ " 23:59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,day1,
                initDate,initDate,0,times,0,
                sqlDate1,sqlDate3
        };
        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = selects+" id, dt,day,among_amount,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }

        String insertSql = "insert into db_lsxy_bi_yunhuni.tb_bi_consume_day("+ values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
    }

    @Override
    public BigDecimal getAmongAmountBetween(Date d1, Date d2) {
        String hql = "from ConsumeDay obj where "
                +StatisticsUtils.getSqlIsNull(null,null, null)+" obj.dt between ?1 and ?2";
        List<ConsumeDay> ds = this.findByCustomWithParams(hql,d1,d2);
        BigDecimal sum = new BigDecimal(0);
        for (ConsumeDay day : ds) {
            if(day!=null && day.getAmongAmount() !=null){
                sum = sum.add(day.getAmongAmount());
            }
        }
        return sum;
    }

    @Override
    public BigDecimal getAmongAmountByDate(Date d) {
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        return getAmongAmountBetween(d1,d2);
    }

    private List<Map<String, Object>> getTops(List<ConsumeDay> ds, String field){
        if(ds == null ){
            return null;
        }
        List<Map<String, Object>> tops = new ArrayList<>();
        for (ConsumeDay d: ds) {
            String tenantName = "";
            Tenant t = tenantService.findById(d.getTenantId());
            if(t!=null && t.getTenantName()!=null){
                tenantName = t.getTenantName();
            }
            Map<String,Object> map = new HashMap<>();
            map.put("name",tenantName);
            map.put("value", ((BigDecimal)BeanUtils.getProperty2(d,field)).doubleValue());
            tops.add(map);
        }
        return tops;
    }

    @Override
    public List<Map<String, Object>> getConsumeTop(int top) {
//        String hql = "from ConsumeDay obj where obj.appId is null and obj.tenantId is not null and type is null ORDER BY obj.sumAmount DESC";
//        List<ConsumeDay> list = this.getTopList(hql,false,top);
//        return getTops(list,"sumAmount");
        String sql = "select FORMAT(sum(among_amount),2) as value ,tenant_id as id ,b.tenant_name as name from db_lsxy_bi_yunhuni.tb_bi_consume_day a LEFT JOIN db_lsxy_base.tb_base_tenant b on a.tenant_id = b.id where a.app_id is null and a.tenant_id is not null and a.type is null group by a.tenant_id ORDER BY sum(a.among_amount) DESC limit 0,?";
        List list = jdbcTemplate.queryForList(sql,top);
        return list;
    }

    @Override
    public List<ConsumeDay> getConsumeDays(String tenantId, String appId, String day) {
        List<ConsumeDay> consumeDays = null;
        if(StringUtils.isBlank(day)){
            throw new IllegalArgumentException("月份为空");
        }
        Date dt = DateUtils.parseDate(day, "yyyy-MM-dd");
        if(StringUtils.isBlank(appId)){
            consumeDays = consumeDayDao.findByTenantIdAndDtAndAppIdIsNullAndTypeNotNull(tenantId,dt);
        }else{
            consumeDays = consumeDayDao.findByTenantIdAndDtAndAppIdAndTypeNotNull(tenantId, dt, appId);
        }

        return consumeDays;
    }

    @Override
    public BigDecimal getAmongAmountByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        String hql = "from ConsumeDay obj where "
                +StatisticsUtils.getSqlIsNull(tenant,appId, null)+" obj.dt between ?1 and ?2";
        List<ConsumeDay> ds = this.findByCustomWithParams(hql,d1,d2);
        BigDecimal sum = new BigDecimal(0);
        for (ConsumeDay day : ds) {
            if(day!=null && day.getAmongAmount() !=null){
                sum = sum.add(day.getAmongAmount());
            }
        }
        return sum;
    }

    @Override
    public BigDecimal getSumAmountByTenant(String tenantId,String time) {
        if(tenantId == null){
            throw new IllegalArgumentException();
        }
        Date startTime = DateUtils.parseDate(time,"yyyy-MM");
        Date endTime = DateUtils.getLastTimeOfMonth(startTime);
        String sql = "select sum(amount) from db_lsxy_bi_yunhuni.tb_bi_consume where tenant_id=:tenant and dt BETWEEN :start AND :end ";
        Query query = em.createNativeQuery(sql);
        query.setParameter("tenant",tenantId);
        query.setParameter("start",startTime);
        query.setParameter("end",endTime);
        Object result = query.getSingleResult();
        if(result!=null){
            return (BigDecimal)result;
        }
        return BigDecimal.ZERO;
    }
}
