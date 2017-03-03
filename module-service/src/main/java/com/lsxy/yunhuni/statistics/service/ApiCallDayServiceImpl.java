package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ApiCallDay;
import com.lsxy.yunhuni.api.statistics.service.ApiCallDayService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.statistics.dao.ApiCallDayDao;
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
import java.util.Date;
import java.util.List;
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
    public void dayStatistics(Date date1, int day1,Date date2,int day2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String sql = " select "+map.get("selects")+" REPLACE(UUID(), '-', '') as id, ? as dt,? as day, "+
                     " sum(among_api) as among_api, " +
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version " +
                     " from db_lsxy_bi_yunhuni.tb_bi_api_call_hour a where tenant_id is not null and app_id is not null and type is not null and a.dt BETWEEN ? AND ?  "+map.get("groupbys");

        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd")+ " 23:59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        //sql对于参数
        Object[] obj = new Object[]{
                sqlDate1,day1,
                initDate,initDate,0,times,0,
                sqlDate1,sqlDate3
        };
        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = map.get("selects")+" id,dt,day,among_api,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }

        String insertSql = " insert into db_lsxy_bi_yunhuni.tb_bi_api_call_day(" + values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
    }

    @Override
    public long getInvokeCountByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfDate(d);
        Date d2 = DateUtils.getLastTimeOfDate(d);
        String hql = "from ApiCallDay obj where "
                +StatisticsUtils.getSqlIsNull(tenant,appId, null)+" obj.dt between ?1 and ?2";
        List<ApiCallDay> ds = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (ApiCallDay day : ds) {
            if(day!=null && day.getAmongApi() !=null){
                sum += day.getAmongApi();
            }
        }
        return sum;
    }

    @Override
    public List<ApiCallDay> list(Object tenantId, Object appId, Object type, Date startTime, Date endTime) {
        String hql = "from ApiCallDay obj where "+StatisticsUtils.getSqlIsNotNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.dt";
        List<ApiCallDay>  list = this.list(hql,startTime,endTime);
        return list;
    }

}
