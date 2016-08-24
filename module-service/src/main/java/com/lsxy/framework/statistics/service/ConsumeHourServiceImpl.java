package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeHour;
import com.lsxy.framework.api.statistics.service.ConsumeHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.ConsumeHourDao;
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
 * 消费小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeHourServiceImpl extends AbstractService<ConsumeHour> implements ConsumeHourService{
    @Autowired
    ConsumeHourDao consumeHourDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ConsumeHour, Serializable> getDao() {
        return consumeHourDao;
    }

    @Override
    public void hourStatistics(Date date1, int hour1, Date date2, int hour2, String[] select,String[] all) throws SQLException {
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
       //拼装sql
        String sql = "insert into db_lsxy_base.tb_base_consume_hour("+selects+" dt,hour,among_amount,create_time,last_time,deleted,sortno,version )" +
                " select "+selects+" ? as dt,? as hour, "+
                " IFNULL(sum(amount),0) as among_amount, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                " from db_lsxy_base.tb_base_consume a where dt BETWEEN ?,? "+groupbys;
       //拼装参数
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Timestamp sqlDate2 = new Timestamp(date2.getTime());
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd HH")+ ":59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,hour1,
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
}
