package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.AppMonth;

import java.sql.SQLException;
import java.util.Date;

/**
 * 应用月统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface AppMonthService extends BaseService<AppMonth> {
    /**
     * 根据当前时间，进行统计
     * @param date 时间yyyy-MM-dd
     * @param month 第几个月 1-12
     * @param select 组合groupby条件
     */
    public void monthStatistics(Date date, int month, String[] select,String[] all) throws SQLException;
}
