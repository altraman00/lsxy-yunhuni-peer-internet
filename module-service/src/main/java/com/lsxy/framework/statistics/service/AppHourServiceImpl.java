package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.AppHour;
import com.lsxy.framework.api.statistics.service.AppHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.statistics.dao.AppHourDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

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
    public void hourStatistics(Date date, int hour) {
        String sql =" insert into db_lsxy_base.tb_base_app_hour(tenant_id,dt,hour,among_on_line,sum_on_line,among_line,sum_line,sum_app_num) ";
        sql +=" select tenant_id, ? as dt,? as hour, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where tenant_id=a.tenant_id and status=1) as sum_on_line, ";
        sql +=" (select count(1) from db_lsxy_bi_yunhuni.tb_bi_app where tenant_id=a.tenant_id and status=2) as sum_line, ";
        sql +=" COUNT(1) as sum_app_num ";
        sql +=" from db_lsxy_bi_yunhuni.tb_bi_app a group by tenant_id ";
        jdbcTemplate.update(sql,new PreparedStatementSetter(){
            public void setValues(PreparedStatement ps) throws SQLException {
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());//进行日期的转换
                ps.setDate(1,sqlDate);
                ps.setInt(2,hour);
            }
        });
    }
}
