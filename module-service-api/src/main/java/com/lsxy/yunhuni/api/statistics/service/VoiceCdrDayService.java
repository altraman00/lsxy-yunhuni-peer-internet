package com.lsxy.yunhuni.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.statistics.model.VoiceCdrDay;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 通话记录统计（session统计）日统计service
 * Created by zhangxb on 2016/8/1.
 */
public interface VoiceCdrDayService extends BaseService<VoiceCdrDay> {
    /**
     * 获取用户某时间的列表数据
     * @param tenantId 对于租户
     * @param appId 应用id
     * @param type 消费类型
     * @param startTime 时间
     * @param endTime 结束时间
     * @return
     */
    public List<VoiceCdrDay> list(String tenantId, String appId,String type,Date startTime, Date endTime);
    /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void dayStatistics(Date date1, int day1, Date date2, int day2, String[] select,String[] all) throws SQLException;

    /**
     * 获取某个时间范围的总的通话时长(话务量)
     * @param
     * @return
     */
    public long getAmongDurationBetween(Date d1,Date d2);
    /**
     * 获取某个时间范围的总的通话时长(扣费话务量)
     * @param
     * @return
     */
    public long getAmongCostTimeBetween(Date d1,Date d2);
    /**
     * 获取某天的总的通话时长(话务量)
     * @param d
     * @return
     */
    public long getAmongDurationByDate(Date d);
    /**
     * 获取某天的总的扣费时长(扣费话务量)
     * @param d
     * @return
     */
    public long getAmongCostTimeByDate(Date d);

    /**
     * 总的会话量排名
     * top
     */
    public List<Map<String,Object>> getCallTop(int top);

    /**
     * 总的话务量排名
     * top
     */
    public List<Map<String,Object>> getDurationTop(int top);
    /**
     * 总的扣费话务量排名
     * top
     */
    public List<Map<String,Object>> getCostTimeTop(int top);

    /**
     * 时间范围内的会话量排名
     * top
     */
    public List<Map<String,Object>> getCallTopByDateBetween(int top,Date d1,Date d2);

    /**
     * 时间范围内的话务量排名
     * top
     */
    public List<Map<String,Object>> getDurationTopByDateBetween(int top,Date d1,Date d2);
    /**
     * 时间范围内的扣费话务量排名
     * top
     */
    public List<Map<String,Object>> getCostTimeTopByDateBetween(int top,Date d1,Date d2);

    /**
     * 获取某个租户某天的通话时长(话务量)
     * @param d
     * @return
     */
    public long getAmongDurationByDateAndTenant(Date d,String tenant,String appId);
    /**
     * 获取某个租户某天的通话时长(扣费话务量)
     * @param d
     * @return
     */
    public long getAmongCostTimeByDateAndTenant(Date d,String tenant,String appId);
    /**
     * 获取某个租户某天的通话次数(会话量)
     * @param d
     * @return
     */
    public long getAmongCallByDateAndTenant(Date d,String tenant,String appId);

    /**
     * 获取某个应用某一天的统计记录
     * @param appId
     * @param currentDay
     * @return
     */
    VoiceCdrDay findByAppIdAndTime(String appId, Date currentDay);

}
