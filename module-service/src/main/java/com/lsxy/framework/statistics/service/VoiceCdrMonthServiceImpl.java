package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.VoiceCdrMonth;
import com.lsxy.framework.api.statistics.service.VoiceCdrMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.VoiceCdrMonthDao;
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
    public void monthStatistics(Date date1, int month1,Date date2,int month2,String[] select) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql =" insert into db_lsxy_base.tb_base_voice_cdr_month("+selects+"dt,month,among_duration,sum_duration,among_connect,sum_connect,among_not_connect,sum_not_connect,among_call,sum_call,create_time,last_time,deleted,sortno,version )" +
                " select "+selects+" ? as dt,? as month, "+
                " IFNULL(sum(among_duration),0) as among_duration," +
                " IFNULL(sum(sum_duration),0) as sum_duration , " +
                " IFNULL(SUM(among_connect),0) as among_connect," +
                " IFNULL(SUM(sum_connect),0) as  sum_connect, " +
                " IFNULL(SUM(among_not_connect),0) as  among_not_connect ," +
                " IFNULL(SUM(sum_not_connect),0) as  sum_not_connect, " +
                " IFNULL(SUM(among_call),0) as among_call,"+
                " IFNULL(SUM(sum_call),0) as sum_call," +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_base.tb_base_voice_cdr_day a where tenant_id is not null and app_id is not null and type is not null and  dt>=? and dt<=? "+groupbys;

        //拼装条件
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Timestamp sqlDate2 = new Timestamp(date2.getTime());
        Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,month1,
                initDate,initDate,1,times,0,
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

}
