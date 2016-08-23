package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ApiCallHour;
import com.lsxy.framework.api.statistics.service.ApiCallHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.ApiCallHourDao;
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
 * api调用小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ApiCallHourServiceImpl extends AbstractService<ApiCallHour> implements ApiCallHourService {
    @Autowired
    ApiCallHourDao apiCallHourDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ApiCallHour, Serializable> getDao() {
        return apiCallHourDao;
    }

    @Override
    public void hourStatistics(Date date1, int hour1,Date date2,int hour2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String sql = " insert into db_lsxy_base.tb_base_api_call_hour("+map.get("selects")+"dt,hour,among_api,create_time,last_time,deleted,sortno,version ) " +
                " select "+map.get("selects")+" ? as dt,? as hour, "+
                " count(1) as among_api, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += "from db_lsxy_bi_yunhuni.tb_bi_api_call_log a where a.call_dt BETWEEN ? AND ?"+map.get("groupbys");

        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd HH")+ ":59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        //sql对于参数
        Object[] obj = new Object[]{sqlDate1,hour1,
                new Timestamp(date2.getTime()),hour2,
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
