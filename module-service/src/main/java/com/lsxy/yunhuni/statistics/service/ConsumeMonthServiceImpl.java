package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ConsumeMonth;
import com.lsxy.yunhuni.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.statistics.dao.ConsumeMonthDao;
import com.lsxy.utils.StatisticsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消费月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeMonthServiceImpl extends AbstractService<ConsumeMonth> implements ConsumeMonthService{
    @Autowired
    ConsumeMonthDao consumeMonthDao;
    @Autowired
    TenantService tenantService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ConsumeMonth, Serializable> getDao() {
        return consumeMonthDao;
    }
    @Override
    public Page<ConsumeMonth> pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize) {
        String hql = "from ConsumeMonth obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.dt,obj.month";
        Page<ConsumeMonth>   page = this.pageList(hql,pageNo,pageSize,startTime,endTime);
        return page;
    }
    @Override
    public Page<ConsumeMonth> compareStartTimeAndEndTimePageList(String tenantId, String appId, String type, Date startTime1, Date endTime1, Date startTime2, Date endTime2, Integer pageNo, Integer pageSize) {
        String hql = "from ConsumeMonth obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" (obj.dt BETWEEN ?1 and ?2  or obj.dt  between ?3 and ?4 ) ORDER BY obj.dt";
        Page<ConsumeMonth>   page =  this.pageList(hql,pageNo,pageSize,startTime1,endTime1,startTime2,endTime2);
        return page;
    }
    @Override
    public List<ConsumeMonth> list(String tenantId, String appId,String type,Date startTime, Date endTime) {
        String hql = "from ConsumeMonth obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.month";
        List<ConsumeMonth>list = this.findByCustomWithParams(hql,startTime,endTime);
        return list;
    }

    @Override
    public String getStartMonthByTenantId(String tenantId) {
        String start = null;
        ConsumeMonth consumeMonth = consumeMonthDao.findFirst1ByTenantIdOrderByDtAsc(tenantId);
        if(consumeMonth != null){
            start = new SimpleDateFormat("yyyy-MM").format(consumeMonth.getDt());
        }
        return start;
    }

    @Override
    public BigDecimal sumAmountByTime(String tenantId, String start, String end){
        BigDecimal amount;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
            Date startTime;
            Date endTime = null;
            String hql;
            if(StringUtils.isNotBlank(start)){
                startTime = format.parse(start);
            }else{
                throw new IllegalArgumentException("参数异常");
            }
            if(StringUtils.isNotBlank(end)){
                endTime = format.parse(end);
            }
            if(endTime != null){
                hql = "select sum(obj.amongAmount) from ConsumeMonth obj where "+StatisticsUtils.getSqlIsNull(tenantId,null, null)+" obj.dt between ?1 and ?2";
            }else{
                hql = "select sum(obj.amongAmount) from ConsumeMonth obj where "+StatisticsUtils.getSqlIsNull(tenantId,null, null)+" obj.dt >= ?1";
            }
            Query query = this.getEm().createQuery(hql);
            query.setParameter(1, startTime);
            if(endTime != null){
                query.setParameter(2, endTime);
            }
            Object obj = query.getSingleResult();
            amount = (BigDecimal) obj;
        }catch (Exception e){
            throw new IllegalArgumentException("参数异常");
        }
        if(amount == null){
            amount = new BigDecimal(0);
        }
        return amount;
    }

    @Override
    public void monthStatistics(Date date1, int month1, Date date2, int month2, String[] select,String[] all) throws SQLException {
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        //拼装sql
        String sql = " SELECT "+selects+"  REPLACE(UUID(), '-', '') as id, ? as dt,? as month, "+
                     " IFNULL(sum(among_amount),0) as among_amount, " +
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                     " from db_lsxy_bi_yunhuni.tb_bi_consume_day a where tenant_id is not null and app_id is not null and type is not null and dt BETWEEN ? AND ? "+groupbys;
        //拼装参数
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Timestamp sqlDate2 = new Timestamp(date2.getTime());
        Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,month1,
                initDate,initDate,0,times,0,
                sqlDate1,sqlDate3
        };
        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = selects+" id,dt,month,among_amount,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }

        String insertSql = "insert into db_lsxy_bi_yunhuni.tb_bi_consume_month("+ values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
    }

    @Override
    public List<ConsumeMonth> getConsumeMonths(String tenantId, String appId, String month) {
        List<ConsumeMonth> consumeMonths = null;
        if(StringUtils.isBlank(month)){
            throw new IllegalArgumentException("月份为空");
        }
        Date dt = DateUtils.parseDate(month, "yyyy-MM");
        if(StringUtils.isBlank(appId)){
            consumeMonths = consumeMonthDao.findByTenantIdAndDtAndAppIdIsNullAndTypeNotNull(tenantId,dt);
        }else{
            consumeMonths = consumeMonthDao.findByTenantIdAndDtAndAppIdAndTypeNotNull(tenantId, dt, appId);
        }

        return consumeMonths;
    }

    private BigDecimal getSumFieldBetween(Date d1,Date d2,String field,String tenantId,String appId,String type){
        String hql = "from ConsumeMonth obj where "
                +StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" obj.dt between ?1 and ?2";
        List<ConsumeMonth> ms = this.findByCustomWithParams(hql,d1,d2);
        BigDecimal sum = new BigDecimal(0);
        for (ConsumeMonth month : ms) {
            if(month!=null && BeanUtils.getProperty2(month,field) !=null){
                sum = sum.add((BigDecimal)BeanUtils.getProperty2(month,field));
            }
        }
        return sum;
    }

    @Override
    public BigDecimal getAmongAmountByDate(Date d) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongAmount",null,null,null);
    }

    @Override
    public BigDecimal getAmongAmountByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongAmount",tenant,appId,null);
    }

    @Override
    public BigDecimal getAmongAmountByDateAndApp(Date d, String app) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongAmount",null,app,null);
    }
}
