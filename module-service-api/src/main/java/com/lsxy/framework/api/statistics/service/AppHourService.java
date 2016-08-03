package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.AppHour;

import java.sql.SQLException;
import java.util.Date;

/**
 * 应用小时统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface AppHourService extends BaseService<AppHour> {
    /**
     * 根据当前时间，进行统计
     *
     * @param date   时间yyyy-MM-dd
     * @param hour   第几个小时0-23
     * @param select 组合groupby条件
     */
    public void hourStatistics(Date date, int hour, String[] select) throws SQLException;
}
