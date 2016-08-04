package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.ConsumeMonth;
import com.lsxy.framework.core.utils.Page;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * 消费月统计Service
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeMonthService extends BaseService<ConsumeMonth> {
    /**
     * 获取用户区间内的分页数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     */
    public Page<ConsumeMonth> pageList(String userName, String appId, String startTime, String endTime, Integer pageNo, Integer pageSize);

    /**
     * 获取用户某时间的列表数据
     * @param userName 用户名
     * @param appId 应用id
     * @param startTime 时间
     * @return
     */
    public List<ConsumeMonth> list(String userName, String appId, String startTime);

    /**
     * 获取用户开始消费的第一个月
     * @param tenantId
     * @return
     */
    String getStartMonthByTenantId(String tenantId);

    /**
     * 根据开始月查询用户的所有消费额
     * @param tenantId
     * @param start 开始时间，不允许为空
     * @param end 结束时间，允许为空
     * @return
     */
    BigDecimal sumAmountByTime(String tenantId, String start, String end);
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void monthStatistics(Date date1, int day1, Date date2, int day2, String[] select) throws SQLException;

}
