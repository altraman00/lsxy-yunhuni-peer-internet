package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeHour;
import com.lsxy.framework.api.statistics.service.ConsumeHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.statistics.dao.ConsumeHourDao;
import com.lsxy.utils.StatisticsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

/**
 * 消费小时统计serviceimpl
 * Created by zhangxb on 2016/7/6.
 */
@Service
public class ConsumeHourServiceImpl extends AbstractService<ConsumeHour> implements ConsumeHourService{
    @Autowired
    ConsumeHourDao consumeHourDao;
    @Override
    public BaseDaoInterface<ConsumeHour, Serializable> getDao() {
        return consumeHourDao;
    }

    @Override
    public void hourStatistics(Date date1, int hour1, Date date2, int hour2, String[] select) throws SQLException {
        Map<String, String> map = StatisticsUtils.getSqlRequirements(select);
        String sql = " insert into db_lsxy_base.tb_base_api_call_hour("+map.get("selects")+"dt,hour,among_api,sum_api,create_time,last_time,deleted,sortno,version ) " +
                " select "+map.get("selects")+" ? as dt,? as hour, "+
                " count(1) as among_api, " +
                " count(1)+IFNULL((select sum_api from db_lsxy_base.tb_base_api_call_hour h where "+map.get("wheres")+" h.dt = ? and h.hour=? ),0) as  sum_api, " +
                " ? as create_time,? as last_time,? as deleted,? as sortno,? as version ";
        sql += "from db_lsxy_bi_yunhuni.tb_bi_api_call_log a where a.call_dt>=? and a.call_dt<=? "+map.get("groupbys");

       // update(date1, hour1,date2,hour2, sql);
    }
}
