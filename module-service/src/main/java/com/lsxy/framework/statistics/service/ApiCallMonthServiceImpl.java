package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ApiCallMonth;
import com.lsxy.framework.api.statistics.service.ApiCallMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.statistics.dao.ApiCallMonthDao;
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
 * api调用月统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ApiCallMonthServiceImpl extends AbstractService<ApiCallMonth> implements ApiCallMonthService {
    @Autowired
    ApiCallMonthDao apiCallMonthDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<ApiCallMonth, Serializable> getDao() {
        return apiCallMonthDao;
    }

    @Override
    public void monthStatistics(Date date1, int month1,Date date2,int month2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql = " insert into db_lsxy_base.tb_base_api_call_month("+selects+"dt,month,among_api,sum_api,create_time,last_time,deleted,sortno,version ) " +
                " select "+selects+" ? as dt,? as month, "+
                " count(1) as among_api, " +
                " count(1)+IFNULL((select sum_api from db_lsxy_base.tb_base_api_call_month h where "+wheres+" h.dt = ? and h.month=? ),0) as  sum_api, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += " from db_lsxy_base.tb_base_api_call_day a where tenant_id is not null and app_id is not null and type is not null and a.dt>=? and a.dt<=? "+groupbys;

        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        //sql对于参数
        Object[] obj = new Object[]{sqlDate1,month1,
                new Timestamp(date2.getTime()),month2,
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
