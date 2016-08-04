package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ApiCallDay;
import com.lsxy.framework.api.statistics.service.ApiCallDayService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.ApiCallDayDao;
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
 * api调用日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ApiCallDayServiceImpl extends AbstractService<ApiCallDay> implements ApiCallDayService {
    @Autowired
    ApiCallDayDao apiCallDayDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ApiCallDay, Serializable> getDao() {
        return apiCallDayDao;
    }

    @Override
    public void dayStatistics(Date date1, int hour1,Date date2,int hour2,String[] select) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String sql = " insert into db_lsxy_base.tb_base_api_call_day("+map.get("selects")+"dt,day,among_api,sum_api,create_time,last_time,deleted,sortno,version ) " +
                " select "+map.get("selects")+" ? as dt,? as day, "+
                " count(1) as among_api, " +
                " count(1)+IFNULL((select sum_api from db_lsxy_base.tb_base_api_call_day h where "+map.get("wheres")+" h.dt = ? and h.day=? ),0) as  sum_api, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += " from db_lsxy_base.tb_base_api_call_hour a where tenant_id is not null and app_id is not null and type is not null and a.dt>=? and a.dt<=? "+map.get("groupbys");

         update(date1, hour1,date2,hour2, sql);
    }

    private void update(final Date date1, final int hour1,final Date date2, final int hour2, String sql) throws  SQLException{
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                Timestamp sqlDate1 = new Timestamp(date1.getTime());//进行日期的转换
                ps.setObject(1,sqlDate1);//统计时间
                ps.setInt(2,hour1);//统计
                Timestamp sqlDate2 = new Timestamp(date2.getTime());//进行日期的转换
                ps.setObject(3,sqlDate2);
                ps.setObject(4,hour2);
                long times = new Date().getTime();
                Timestamp initDate = new Timestamp(times);
                ps.setObject(5,initDate);
                ps.setObject(6,initDate);
                ps.setObject(7,1);
                ps.setObject(8,times);
                ps.setObject(9,0);
                ps.setObject(10,sqlDate1);
                Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd")+ " 23:59:59","yyyy-MM-dd HH:mm:ss");
                Timestamp sqlDate3 = new Timestamp(date3.getTime());
                ps.setObject(11,sqlDate3);
            }
        });
    }
}
