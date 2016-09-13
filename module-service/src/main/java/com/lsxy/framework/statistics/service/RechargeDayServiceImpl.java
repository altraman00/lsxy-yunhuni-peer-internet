package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.RechargeDay;
import com.lsxy.framework.api.statistics.service.RechargeDayService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.statistics.dao.RechargeDayDao;
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
 * 订单日统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class RechargeDayServiceImpl extends AbstractService<RechargeDay> implements RechargeDayService {
    @Autowired
    RechargeDayDao rechargeDayDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<RechargeDay, Serializable> getDao() {
        return rechargeDayDao;
    }

    @Override
    public void dayStatistics(Date date1, int day1,Date date2,int day2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String sql = "insert into db_lsxy_bi_yunhuni.tb_bi_recharge_day("+selects+" id,dt,day,among_amount,among_num,create_time,last_time,deleted,sortno,version)" +
                " select "+selects+"  REPLACE(UUID(), '-', '') as id, ? as dt,? as day, "+
                " IFNULL(sum(among_amount),0) as among_amount, " +
                " IFNULL(sum(among_num),0) as among_num, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += " from db_lsxy_bi_yunhuni.tb_bi_recharge_hour a where tenant_id is not null and a.dt between ? AND ? " +groupbys;
        //拼装条件
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.formatDate(date1,"yyyy-MM-dd")+ " 23:59:59","yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,day1,
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
