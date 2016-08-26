package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.RechargeMonth;
import com.lsxy.framework.api.statistics.service.RechargeMonthService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.statistics.dao.RechargeMonthDao;
import com.lsxy.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 订单月统计serviceimpl
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
    public void monthStatistics(Date date1, int month1,Date date2,int month2,String[] select,String[] all) throws  SQLException{
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select,all);
        String selects = map.get("selects");
        String groupbys = map.get("groupbys");
        String sql = "insert into db_lsxy_base.tb_base_recharge_month("+selects+"dt,month,among_amount,among_num,create_time,last_time,deleted,sortno,version)" +
                " select "+selects+" ? as dt,? as day, "+
                " IFNULL(sum(among_amount),0) as among_amount, " +
                " IFNULL(sum(among_num),0) as  among_num, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += " from db_lsxy_base.tb_base_recharge_day a where tenant_id is not null and a.dt between ?,?  " +groupbys;
        //拼装条件
        Timestamp sqlDate1 = new Timestamp(date1.getTime());
        long times = new Date().getTime();
        Timestamp initDate = new Timestamp(times);
        Date date3 = DateUtils.parseDate(DateUtils.getMonthLastTime(date1),"yyyy-MM-dd HH:mm:ss");
        Timestamp sqlDate3 = new Timestamp(date3.getTime());
        Object[] obj = new Object[]{
                sqlDate1,month1,
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

    private BigDecimal getSumFieldBetween(Date d1,Date d2,String field,String tenantId){
        String hql = "from RechargeMonth obj where obj.tenantId = '"+tenantId+"' and obj.dt between ?1 and ?2";
        List<RechargeMonth> ms = this.findByCustomWithParams(hql,d1,d2);
        BigDecimal sum = new BigDecimal(0);
        for (RechargeMonth month : ms) {
            if(month!=null && BeanUtils.getProperty2(month,field) !=null){
                sum.add((BigDecimal)BeanUtils.getProperty2(month,field));
            }
        }
        return sum;
    }

    /**
     * 获取某月的某个租户的充值额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountByDateAndTenant(Date d, String tenant){
        Date d1 = DateUtils.getFirstTimeOfMonth(d);
        Date d2 = DateUtils.getLastTimeOfMonth(d);
        return getSumFieldBetween(d1,d2,"among_amount",tenant);
    }
}
