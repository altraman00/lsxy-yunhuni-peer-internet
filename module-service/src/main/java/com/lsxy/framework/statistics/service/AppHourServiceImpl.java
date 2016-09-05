package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.AppHour;
import com.lsxy.framework.api.statistics.service.AppHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.statistics.dao.AppHourDao;
import com.lsxy.utils.StatisticsUtils;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

/**
 * 消费小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class AppHourServiceImpl extends AbstractService<AppHour> implements AppHourService {
    @Autowired
    AppHourDao appHourDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<AppHour, Serializable> getDao() {
        return appHourDao;
    }

    @Override
    public void hourStatistics(Date date, int hour,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String sql =" insert into db_lsxy_base.tb_base_app_hour("+map.get("selects")+"dt,hour,sum_on_line,sum_line,sum_app_num,create_time,last_time,deleted,sortno,version) ";
        sql +=" select "+map.get("selects")+" ? as dt,? as hour, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+map.get("wheres")+" status=1) as sum_on_line, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+map.get("wheres")+" status=2) as sum_line, ";
        sql +=" COUNT(1) as sum_app_num ,";
        sql +=" ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql +=" from db_lsxy_bi_yunhuni.tb_bi_app a "+map.get("groupbys");
        //拼装需要的参数
        Timestamp sqlDate = new Timestamp(date.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Object[] obj = new Object[]{
                sqlDate,hour,initDate,initDate,0,times,0
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
