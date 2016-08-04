package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.RechargeMonth;
import com.lsxy.framework.api.statistics.service.RechargeMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.RechargeMonthDao;
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
 * 订单统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class RechargeMonthServiceImpl extends AbstractService<RechargeMonth> implements RechargeMonthService {
    @Autowired
    RechargeMonthDao rechargeMonthDao;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Override
    public BaseDaoInterface<RechargeMonth, Serializable> getDao() {
        return rechargeMonthDao;
    }

    @Override
    public void monthStatistics(Date date1, int month1,Date date2,int month2,String[] select) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String sql = "insert into db_lsxy_base.tb_base_recharge_month("+selects+"dt,month,among_amount,sum_amount,sum_num,create_time,last_time,deleted,sortno,version)" +
                " select "+selects+" ? as dt,? as day, "+
                " IFNULL(sum(among_amount),0) as among_amount, " +
                " IFNULL(sum(sum_amount),0) as  sum_amount, " +
                " IFNULL(sum(sum_num),0) as sum_num, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += " from db_lsxy_base.tb_base_recharge_day a where tenant_id is not null and a.dt>=? and a.dt<=? " +groupbys;
        update(date1, month1,date2,month2, sql);
    }

    private void update(final Date date1, final int month1,final Date date2, final int month2, String sql) throws  SQLException{
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                Timestamp sqlDate1 = new Timestamp(date1.getTime());//进行日期的转换
                ps.setObject(1,sqlDate1);
                ps.setInt(2,month1);
                long times = new Date().getTime();
                Timestamp initDate = new Timestamp(times);
                ps.setObject(3,initDate);
                ps.setObject(4,initDate);
                ps.setObject(5,1);
                ps.setObject(6,times);
                ps.setObject(7,0);
                ps.setObject(8,sqlDate1);
                Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
                Timestamp sqlDate3 = new Timestamp(date3.getTime());
                ps.setObject(9,sqlDate3);
            }
        });
    }


}
