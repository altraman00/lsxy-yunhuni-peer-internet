package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.ApiCallDay;

import java.sql.SQLException;
import java.util.Date;

/**
 * api调用小时统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface ApiCallDayService extends BaseService<ApiCallDay> {
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void dayStatistics(Date date1, int day1,Date date2,int day2, String[] select,String[] all) throws SQLException;}
