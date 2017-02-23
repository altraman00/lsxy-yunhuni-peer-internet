package com.lsxy.yunhuni.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.statistics.model.ApiCallDay;
import com.lsxy.yunhuni.api.statistics.model.ApiCallMonth;
import com.lsxy.yunhuni.api.statistics.service.ApiCallMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.statistics.dao.ApiCallMonthDao;
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
        String sql = " select "+selects+" REPLACE(UUID(), '-', '') as id, ? as dt,? as month, "+
                     " sum(among_api) as among_api, " +
                     " ? as create_time,? as last_time,? as deleted,? as sortno,? as version " +
                     " from db_lsxy_bi_yunhuni.tb_bi_api_call_day a where tenant_id is not null and app_id is not null and type is not null and a.dt BETWEEN ? AND ? "+groupbys;

        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        //sql对于参数
        Object[] obj = new Object[]{sqlDate1,month1,
                initDate,initDate,0,times,0,
                sqlDate1,sqlDate3
        };

        Query query = getEm().createNativeQuery(sql);
        for(int i=0;i<obj.length;i++){
            query.setParameter(i+1,obj[i]);
        }
        List resultList = query.getResultList();

        String values = selects+" id,dt,month,among_api,create_time,last_time,deleted,sortno,version";
        String valuesMark = "";
        int length = values.split(",").length;
        for(int i = 0;i<length;i++){
            if(i == length -1){
                valuesMark += "?";
            }else{
                valuesMark += "?,";
            }
        }

        String insertSql = " insert into db_lsxy_bi_yunhuni.tb_bi_api_call_month("+ values + ") values ("+valuesMark+")";

        if(resultList != null && resultList.size() > 0){
            jdbcTemplate.batchUpdate(insertSql,resultList);
        }
    }

    @Override
    public long getInvokeCountByDateAndTenant(Date d, String tenant,String appId) {
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        String hql = "from ApiCallMonth obj where "
                +StatisticsUtils.getSqlIsNull(tenant,appId, null)+" obj.dt between ?1 and ?2";
        List<ApiCallMonth> ds = this.findByCustomWithParams(hql,d1,d2);
        long sum = 0;
        for (ApiCallMonth month : ds) {
            if(month!=null && month.getAmongApi() !=null){
                sum += month.getAmongApi();
            }
        }
        return sum;
    }

    @Override
    public List<ApiCallMonth> list(Object tenantId, Object appId, Object type, Date startTime, Date endTime) {
        String hql = "from ApiCallMonth obj where "+StatisticsUtils.getSqlIsNotNull(tenantId,appId, type)+"  obj.dt>=?1 and obj.dt<=?2 ORDER BY obj.dt";
        List<ApiCallMonth>  list = this.list(hql,startTime,endTime);
        return list;
    }

}
