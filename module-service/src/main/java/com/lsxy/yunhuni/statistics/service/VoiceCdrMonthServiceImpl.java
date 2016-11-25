package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.session.model.CallSession;
import com.lsxy.yunhuni.api.statistics.model.VoiceCdrMonth;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.statistics.dao.VoiceCdrMonthDao;
import com.lsxy.utils.StatisticsUtils;
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
 * 通话记录统计（session统计）月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class VoiceCdrMonthServiceImpl extends AbstractService<VoiceCdrMonth> implements VoiceCdrMonthService {
    @Autowired
    VoiceCdrMonthDao voiceCdrMonthDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<VoiceCdrMonth, Serializable> getDao() {
        return voiceCdrMonthDao;
    }
    @Override
    public List<VoiceCdrMonth> list(String tenantId, String appId,String type,Date startTime, Date endTime) {
        String[] types = null;
        if(type!=null){
            types = new String[]{type};
        }
        if(App.PRODUCT_CALL_CENTER.equals(type)){
            types = CallSession.PRODUCT_CODE;
        }
        String hql = "from VoiceCdrMonth obj where "+StatisticsUtils.getSqlIsNull2(tenantId,appId, types)+"  obj.dt between ?1 and ?2 ORDER BY obj.month";
        List<VoiceCdrMonth>list = this.list(hql,startTime,endTime);
        return list;
    }
    @Override
    public void monthStatistics(Date date1, int month1,Date date2,int month2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql =" insert into db_lsxy_bi_yunhuni.tb_bi_voice_cdr_month("+selects+" id,dt,month,among_cost_time,among_duration,among_connect,among_not_connect,among_call,create_time,last_time,deleted,sortno,version )" +
                " select "+selects+"   REPLACE(UUID(), '-', '') as id,? as dt,? as month, "+
                " IFNULL(sum(among_cost_time),0) as among_cost_time," +
                " IFNULL(sum(among_duration),0) as among_duration," +
                " IFNULL(SUM(among_connect),0) as among_connect," +
                " IFNULL(SUM(among_not_connect),0) as  among_not_connect ," +
                " IFNULL(SUM(among_call),0) as among_call,"+
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_bi_yunhuni.tb_bi_voice_cdr_day a where tenant_id is not null and app_id is not null and type is not null and  dt BETWEEN ? AND ? "+groupbys;

        //拼装条件
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
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                for(int i=0;i<obj.length;i++){
                    ps.setObject(i+1,obj[i]);
                }
            }
        });
    }

    private long getSumFieldBetween(Date d1,Date d2,String field,String tenantId,String appId,String type){
        String hql = "from VoiceCdrMonth obj where "
                +StatisticsUtils.getSqlIsNull(tenantId,appId, type)+" obj.dt between ?1 and ?2";
        List<VoiceCdrMonth> ms = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (VoiceCdrMonth month : ms) {
            if(month!=null && BeanUtils.getProperty2(month,field) !=null){
                sum += (long)BeanUtils.getProperty2(month,field);
            }
        }
        return sum;
    }

    @Override
    public long getAmongDurationByDate(Date d) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongDuration",null,null,null);
    }

    @Override
    public long getAmongCostTimeByDate(Date d) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongCostTime",null,null,null);
    }

    @Override
    public long getAmongDurationByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongDuration",tenant,appId,null);
    }

    @Override
    public long getAmongCostTimeByDateAndTenant(Date d, String tenant, String appId) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongCostTime",tenant,appId,null);
    }

    @Override
    public long getAmongCallByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongCall",tenant,appId,null);
    }

    @Override
    public long getAmongConnectByDateAndTenant(Date d, String tenant) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongConnect",tenant,null,null);
    }

    @Override
    public long getAmongDurationByDateAndApp(Date d, String app) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongDuration",null,app,null);
    }

    @Override
    public long getAmongCostTimeByDateAndApp(Date d, String app) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongCostTime",null,app,null);
    }

    @Override
    public long getAmongCallByDateAndApp(Date d, String app) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"amongCall",null,app,null);
    }
}
