package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.ConsumeDay;
import com.lsxy.framework.core.utils.Page;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 消费日统计service
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeDayService extends BaseService<ConsumeDay> {
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public Page<ConsumeDay> pageList(String userName, String appId, String startTime, String endTime,Integer pageNo,Integer pageSize);

    /**
     * 获取用户某时间的列表数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    public List<ConsumeDay> list(String userName, String appId, String startTime);

    /**
     * 获取一定时间的统计数据的总数 时间为月份 yyyy-MM
     * @param userName
     * @param appId 应用ID，可为空
     * @param startTime 开始时间，不可为空
     * @param endTime 结果时间，不可为空
     * @return
     */
    Long countByTime(String userName, String appId, String startTime, String endTime);

    /**
     * 获取一定时间的统计数据 时间为月份 yyyy-MM
     * @param userName
     * @param appId 应用ID，可为空
     * @param startTime 开始时间，不可为空
     * @param endTime 结果时间，不可为空
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<ConsumeDay> pageListByTime(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize);
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void dayStatistics(Date date1, int day1, Date date2, int day2, String[] select) throws SQLException;
}
