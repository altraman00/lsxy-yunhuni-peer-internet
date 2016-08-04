package com.lsxy.framework.statistics.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.statistics.model.ConsumeHour;
import com.lsxy.framework.api.statistics.service.ConsumeHourService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.statistics.dao.ConsumeHourDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;

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

    }
}
