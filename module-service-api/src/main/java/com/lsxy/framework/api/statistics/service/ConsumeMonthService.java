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
    public Page<ConsumeMonth> pageList(String tenantId, String appId,String type,Date startTime, Date endTime,Integer pageNo,Integer pageSize);

    /**
     * 获取用户某时间的列表数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @return
     */
    public List<ConsumeMonth> list(String tenantId, String appId,String type,Date startTime, Date endTime);

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
     * @param month1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param month2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void monthStatistics(Date date1, int month1, Date date2, int month2, String[] select) throws SQLException;

    /**
     * 获取某月的总的消费额
     * @param d
     * @return
     */
    public BigDecimal getAmongAmountByDate(Date d);

    /**
     * 获取某个月的月结账单
     * @param tenantId 租户ID
     * @param appId 应用ID（为空""或null则表示全部应用）
     * @param month 月份
     * @return
     */
    List<ConsumeMonth> getConsumeMonths(String tenantId, String appId, String month);

}
