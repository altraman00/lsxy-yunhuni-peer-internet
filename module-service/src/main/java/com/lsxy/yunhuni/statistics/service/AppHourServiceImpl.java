package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.AppHour;
import com.lsxy.yunhuni.api.statistics.service.AppHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.statistics.dao.AppHourDao;
import com.lsxy.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
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
        String sql = " select "+map.get("selects")+" REPLACE(UUID(), '-', '') as id, ? as dt,? as hour, "+
                     " (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+map.get("wheres")+" status=1) as sum_on_line, "+
                     " (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where "+map.get("wheres")+" status=2) as sum_line, "+
                     " COUNT(1) as sum_app_num ,"+
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version "+
                     " from db_lsxy_bi_yunhuni.tb_bi_app a "+map.get("groupbys");
        //拼装需要的参数
        Timestamp sqlDate = new Timestamp(date.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Object[] obj = new Object[]{
                sqlDate,hour,initDate,initDate,0,times,0
        };

        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = map.get("selects")+"id,dt,hour,sum_on_line,sum_line,sum_app_num,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }

        String insertSql =" insert into db_lsxy_bi_yunhuni.tb_bi_app_hour("+ values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
    }

}
