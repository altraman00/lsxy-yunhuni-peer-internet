package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.AppDay;

import java.sql.SQLException;
import java.util.Date;

/**
 * 应用日统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface AppDayService extends BaseService<AppDay> {
    /**
     * 根据当前时间，进行统计
     * @param date 时间yyyy-MM-dd
     * @param day 第几天 1-31
     * @param select 组合groupby条件
     */
    public void dayStatistics(Date date, int day, String[] select,String[] all) throws SQLException;
}
