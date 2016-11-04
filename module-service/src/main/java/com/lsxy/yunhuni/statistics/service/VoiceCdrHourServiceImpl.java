package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.VoiceCdrHour;
import com.lsxy.yunhuni.api.statistics.service.VoiceCdrHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.statistics.dao.VoiceCdrHourDao;
import com.lsxy.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 通话记录统计（session统计）小时统计serviceimpl
 * Created hangxb on 2016/7/6.
 */
@Service
public class VoiceCdrHourServiceImpl extends AbstractService<VoiceCdrHour> implements VoiceCdrHourService {
    @Autowired
    VoiceCdrHourDao voiceCdrHourDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<VoiceCdrHour, Serializable> getDao() {
        return voiceCdrHourDao;
    }

    @Override
    public void hourStatistics(Date date1, int hour1,Date date2,int hour2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql =" insert into db_lsxy_bi_yunhuni.tb_bi_voice_cdr_hour("+selects+" id,dt,hour,among_cost_time,among_duration,among_connect,among_not_connect,among_call,create_time,last_time,deleted,sortno,version )" +
                " select "+selects+"  REPLACE(UUID(), '-', '') as id, ? as dt,? as day, "+
                " IFNULL(sum(cost_time_long),0) as among_cost_time," +
                " IFNULL(sum(call_time_long),0) as among_duration," +
                " (select count(1) from db_lsxy_bi_yunhuni.tb_bi_voice_cdr c1 where "+wheres+"  c1.call_ack_dt is not null AND call_end_dt BETWEEN ? AND ? ) as among_connect," +
                " (select count(1) from db_lsxy_bi_yunhuni.tb_bi_voice_cdr c1 where "+wheres+" c1.call_ack_dt is null AND call_end_dt BETWEEN ? AND ?) as  among_not_connect ," +
                " count(1) as among_call,"+
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_bi_yunhuni.tb_bi_voice_cdr a where call_end_dt BETWEEN ? AND ? "+groupbys;

        //拼装条件
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Timestamp sqlDate2 = new Timestamp(date2.getTime());
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd HH")+ ":59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,hour1,
                sqlDate1,sqlDate3,sqlDate1,sqlDate3,
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
    public Map<String,Object> calAverageCall(String tenantId){
        Object[] argus = {tenantId};
        String sql = "SELECT IFNULL(SUM(cdr.among_cost_time),0) duration,IFNULL(SUM(cdr.among_connect),0) connect_num,IFNULL(SUM(cdr.among_call),0) call_num " +
                "FROM db_lsxy_bi_yunhuni.tb_bi_voice_cdr_hour cdr WHERE cdr.tenant_id = ? AND cdr.app_id IS NULL AND cdr.type IS NULL";
        Map<String, Object> map = jdbcTemplate.queryForMap(sql, argus);
        Map<String,Object> result = new HashMap<>();
        //lineAverageCallTime lineLinkRate
        if(new BigDecimal(0).compareTo((BigDecimal)map.get("connect_num")) == -1){
            BigDecimal divide = ((BigDecimal) map.get("duration")).divide((BigDecimal) map.get("connect_num"),0, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(60), 0, BigDecimal.ROUND_HALF_UP);
            result.put("lineAverageCallTime",divide.longValue());
        }else{
            result.put("lineAverageCallTime",0);
        }
        if(new BigDecimal(0).compareTo((BigDecimal)map.get("connect_num")) == -1){
            BigDecimal divide = ((BigDecimal) map.get("connect_num")).divide((BigDecimal) map.get("call_num"), 4, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            result.put("lineLinkRate",divide.doubleValue());
        }else{
            result.put("lineLinkRate",0.0);
        }
        return result;
    }

    @Override
    public VoiceCdrHour findByAppIdAndTime(String appId, Date currentHour) {
        return voiceCdrHourDao.findFirstByAppIdAndDtAndTenantIdIsNullAndTypeIsNull(appId,currentHour);
    }

}
