package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.RechargeHour;

import java.sql.SQLException;
import java.util.Date;

/**
 * 订单记录小时统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface RechargeHourService extends BaseService<RechargeHour> {
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd HH
     * @param hour1 第几个小时
     * @param date2 前一天的时间 yyyy-MM-dd HH
     * @param hour2 前一个小时
     * @param select 组合groupby条件
     */
    public void hourStatistics(Date date1, int hour1, Date date2, int hour2, String[] select,String[] all) throws SQLException;
}
