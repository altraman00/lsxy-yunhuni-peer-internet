package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.VoiceCdrDay;
import com.lsxy.framework.api.statistics.service.VoiceCdrDayService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.VoiceCdrDayDao;
import com.lsxy.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 通话记录统计（session统计）日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class VoiceCdrDayServiceImpl extends AbstractService<VoiceCdrDay> implements VoiceCdrDayService {
    @Autowired
    VoiceCdrDayDao voiceCdrDayDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Override
    public BaseDaoInterface<VoiceCdrDay, Serializable> getDao() {
        return voiceCdrDayDao;
    }

    @Override
    public List<VoiceCdrDay> list(String tenantId, String appId,String type,Date startTime, Date endTime) {
        String hql = "from VoiceCdrDay obj where "+StatisticsUtils.getSqlIsNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.day";
        List<VoiceCdrDay>  list = this.list(hql,startTime,endTime);
        return list;
    }

    @Override
    public void dayStatistics(Date date1, int day1,Date date2,int day2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql =" insert into db_lsxy_base.tb_base_voice_cdr_day("+selects+"dt,day,among_duration,among_connect,among_not_connect,among_call,create_time,last_time,deleted,sortno,version )" +
                " select "+selects+" ? as dt,? as day, "+
                " IFNULL(sum(among_duration),0) as among_duration," +
                " IFNULL(SUM(among_connect),0) as among_connect," +
                " IFNULL(SUM(among_not_connect),0) as  among_not_connect ," +
                " IFNULL(SUM(among_call),0) as among_call,"+
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_base.tb_base_voice_cdr_hour a where tenant_id is not null and app_id is not null and type is not null and  dt BETWEEN ? AND ?"+groupbys;

        //拼装条件
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

    public long getAmongDurationBetween(Date d1,Date d2){
        String hql = "from VoiceCdrDay obj where "
                +StatisticsUtils.getSqlIsNull(null,null, null)+" obj.dt between ?1 and ?2";
        List<VoiceCdrDay> ds = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (VoiceCdrDay day : ds) {
            if(day!=null && day.getAmongDuration() !=null){
                sum += day.getAmongDuration();
            }
        }
        return sum;
    }

    public long getAmongDurationByDate(Date d){
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        return getAmongDurationBetween(d1,d2);
    }

    private List<Map<String, Object>> getTops(List<VoiceCdrDay> ds,String field,Integer factor){
        if(ds == null ){
            return null;
        }
        List<Map<String, Object>> tops = new ArrayList<>();
        for (VoiceCdrDay d: ds) {
            String tenantName = "";
            Tenant t = tenantService.findById(d.getTenantId());
            if(t!=null && t.getTenantName()!=null){
                tenantName = t.getTenantName();
            }
            Map<String,Object> map = new HashMap<>();
            map.put("name",tenantName);
            if(factor == null){
                map.put("value", (Long)BeanUtils.getProperty2(d,field));
            }else{//单位转换
                map.put("value", (long)Math.round(((Long)BeanUtils.getProperty2(d,field))/60));
            }
            tops.add(map);
        }
        return tops;
    }

    @Override
    public List<Map<String, Object>> getCallTop(int top) {
        String hql = "from VoiceCdrDay obj where obj.appId is null and obj.tenantId is not null and type is null ORDER BY obj.sumCall DESC";
        List<VoiceCdrDay> list = this.getTopList(hql,false,top);
        return getTops(list,"sumCall",null);
    }

    @Override
    public List<Map<String, Object>> getDurationTop(int top) {
        String hql = "from VoiceCdrDay obj where obj.appId is null and obj.tenantId is not null and type is null ORDER BY obj.sumDuration DESC";
        List<VoiceCdrDay> list = this.getTopList(hql,false,top);
        return getTops(list,"sumDuration",60);
    }

    @Override
    public List<Map<String, Object>> getCallTopByDateBetween(int top, Date d1, Date d2) {
        String sql = "select tenant_id,sum(among_call)among_call from tb_base_voice_cdr_day where app_id is null and tenant_id is not null and type is null and dt between :d1 and :d2 group BY tenant_id order by among_call desc";
        Query query = em.createNativeQuery(sql,VoiceCdrDay.class);
        query.setParameter("d1", d1);
        query.setParameter("d2", d2);
        query.setFirstResult(0);
        query.setMaxResults(top);
        List<VoiceCdrDay> list = query.getResultList();
        return getTops(list,"amongCall",null);
    }

    @Override
    public List<Map<String, Object>> getDurationTopByDateBetween(int top, Date d1, Date d2) {
        String sql = "select tenant_id,sum(among_duration)among_call from tb_base_voice_cdr_day where app_id is null and tenant_id is not null and type is null and dt between :d1 and :d2 group BY tenant_id order by among_duration desc";
        Query query = em.createNativeQuery(sql,VoiceCdrDay.class);
        query.setParameter("d1", d1);
        query.setParameter("d2", d2);
        query.setFirstResult(0);
        query.setMaxResults(top);
        List<VoiceCdrDay> list = query.getResultList();
        return getTops(list,"amongDuration",60);
    }

    @Override
    public long getAmongDurationByDateAndTenant(Date d, String tenant) {
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        String hql = "from VoiceCdrDay obj where "
                +StatisticsUtils.getSqlIsNull(tenant,null, null)+" obj.dt between ?1 and ?2";
        List<VoiceCdrDay> ds = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (VoiceCdrDay day : ds) {
            if(day!=null && day.getAmongDuration() !=null){
                sum += day.getAmongDuration();
            }
        }
        return sum;
    }

    @Override
    public long getAmongCallByDateAndTenant(Date d, String tenant) {
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        String hql = "from VoiceCdrDay obj where "
                +StatisticsUtils.getSqlIsNull(tenant,null, null)+" obj.dt between ?1 and ?2";
        List<VoiceCdrDay> ds = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (VoiceCdrDay day : ds) {
            if(day!=null && day.getAmongCall() !=null){
                sum += day.getAmongCall();
            }
        }
        return sum;
    }

}
