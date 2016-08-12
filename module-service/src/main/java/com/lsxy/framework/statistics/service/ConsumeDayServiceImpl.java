package com.lsxy.framework.statistics.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeDay;
import com.lsxy.framework.api.statistics.service.ConsumeDayService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.statistics.dao.ConsumeDayDao;
import com.lsxy.utils.StatisticsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    @Override
    public BaseDaoInterface<ConsumeDay, Serializable> getDao() {
        return consumeDayDao;
    }

    @Override
    public Page<ConsumeDay> pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize) {
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" obj.dt>=?1 and obj.dt<=?2 ) ORDER BY obj.dt,obj.day";
        Page<ConsumeDay>   page =  this.pageList(hql,pageNo,pageSize,startTime,endTime);
        return page;
    }

    @Override
    public List<ConsumeDay> list(String tenantId, String appId,String type,Date startTime, Date endTime) {
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.day";
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
        Date start = DateUtils.parseDate(startTime,"yyyy-MM");
        Date end = DateUtils.parseDate(nextMonth,"yyyy-MM");
        String hql = "from ConsumeDay obj where "+StatisticsUtils.getSqlIsNull(tenant.getId(),appId, null)+" obj.dt>=?1 and obj.dt<?2";
        return getPageList(hql, pageNo -1, pageSize, start, end);
    }

    @Override
    public void dayStatistics(Date date1, int day1, Date date2, int day2, String[] select) throws SQLException {
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        //拼装sql
        String sql = "insert into db_lsxy_base.tb_base_consume_day("+selects+" dt,day,among_amount,sum_amount,create_time,last_time,deleted,sortno,version )" +
                " SELECT "+selects+" ? as dt,? as day, "+
                " IFNULL(sum(among_amount),0) as among_amount, " +
                " IFNULL(sum(sum_amount),0) as  sum_amount, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_base.tb_base_consume_hour a where tenant_id is not null and app_id is not null and type is not null and dt>=? and dt<=? "+groupbys;
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
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                for(int i=0;i<obj.length;i++){
                    ps.setObject(i+1,obj[i]);
                }
            }
        });
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
}
