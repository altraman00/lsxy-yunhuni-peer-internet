package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.ConsumeDay;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.Page;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消费日统计service
 * Created by zhangxb on 2016/7/6.
 */
public interface ConsumeDayService extends BaseService<ConsumeDay> {
    /**
     * 获取分页数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @return
     */
    public Page<ConsumeDay> pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize);

    /**
     * 获取用户某时间的列表数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @return
     */
    public List<ConsumeDay> list(String tenantId, String appId,String type,Date startTime, Date endTime);

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


    /**
     * 获取某个时间范围的总的消费额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountBetween(Date d1, Date d2);

    /**
     * 获取某天的总的消费额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountByDate(Date d);

    public List<Map<String, Double>> getConsumeTop(int top);

    /**
     * 获取某一天的日结账单
     * @param tenantId 租户ID
     * @param appId 应用ID（为空""或null则表示全部应用）
     * @param month 月份
     * @return
     */
    List<ConsumeDay> getConsumeDays(String tenantId, String appId, String month);

    /**
     * 获取某个租户某天的消费额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountByDateAndTenant(Date d,String tenant);

    /**
     * 获取某个租户总的消费额
     * @param tenantId
     * @return
     */
    public BigDecimal getSumAmountByTenant(String tenantId);

}
