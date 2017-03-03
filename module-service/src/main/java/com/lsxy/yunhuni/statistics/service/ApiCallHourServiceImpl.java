package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ApiCallHour;
import com.lsxy.yunhuni.api.statistics.service.ApiCallHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.statistics.dao.ApiCallHourDao;
import com.lsxy.utils.StatisticsUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
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
        String sql = " select "+map.get("selects")+" REPLACE(UUID(), '-', '') as id, ? as dt,? as hour, "+
                     " count(1) as among_api, " +
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version " +
                     " from db_lsxy_bi_yunhuni.tb_bi_api_log a where a.last_time BETWEEN ? AND ?"+map.get("groupbys");

        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd HH")+ ":59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        //sql对于参数
        Object[] obj = new Object[]{
                sqlDate1,hour1,
                initDate,initDate,0,times,0,
                sqlDate1,sqlDate3
        };
        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = map.get("selects") + " id,dt,hour,among_api,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }
        String insertSql = " insert into db_lsxy_bi_yunhuni.tb_bi_api_call_hour(" + values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }

    }

}
