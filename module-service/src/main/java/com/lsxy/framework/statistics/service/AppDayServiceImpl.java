package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.AppDay;
import com.lsxy.framework.api.statistics.model.AppHour;
import com.lsxy.framework.api.statistics.service.AppDayService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.statistics.dao.AppDayDao;
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
public class AppDayServiceImpl extends AbstractService<AppDay> implements AppDayService {
    @Autowired
    AppDayDao appDayDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<AppDay, Serializable> getDao() {
        return appDayDao;
    }

    @Override
    public void dayStatistics(Date date, int day,String[] select) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String wheres = map.get("wheres");
        String sql =" insert into db_lsxy_base.tb_base_app_day("+selects+"dt,day,sum_on_line,sum_line,sum_app_num,create_time,last_time,deleted,sortno,version) ";
        sql +=" select "+selects+" ? as dt,? as day, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+wheres+" status=1) as sum_on_line, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+wheres+" status=2) as sum_line, ";
        sql +=" COUNT(1) as sum_app_num ,";
        sql +=" ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql +=" from db_lsxy_bi_yunhuni.tb_bi_app a "+groupbys;
        //拼装需要的参数
        Timestamp sqlDate = new Timestamp(date.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Object[] obj = new Object[]{
                sqlDate,day,initDate,initDate,0,times,0
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
