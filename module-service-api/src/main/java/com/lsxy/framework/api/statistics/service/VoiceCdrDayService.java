package com.lsxy.framework.api.statistics.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.statistics.model.VoiceCdrDay;
import com.lsxy.framework.api.tenant.model.Tenant;

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
    public List<VoiceCdrDay> list(String tenantId, String appId,String type,Date startTime, Date endTime); /**
     * 根据当前时间，进行统计
     * @param date1 时间yyyy-MM-dd
     * @param day1 第几天 1-31
     * @param date2 前一天的时间 yyyy-MM-dd
     * @param day2 前一天是第几天 1-31
     * @param select 组合groupby条件
     */
    public void dayStatistics(Date date1, int day1, Date date2, int day2, String[] select) throws SQLException;


    /**
     * 获取某个时间范围的总的通话时长(话务量)
     * @param d
     * @return
     */
    public long getAmongDurationBetween(Date d1,Date d2);

    /**
     * 获取某天的总的通话时长(话务量)
     * @param d
     * @return
     */
    public long getAmongDurationByDate(Date d);

    /**
     * 总的会话量排名
     * top
     */
    public List<Map<String,Long>> getCallTop(int top);

    /**
     * 总的话务量排名
     * top
     */
    public List<Map<String,Long>> getDurationTop(int top);

    /**
     * 时间范围内的会话量排名
     * top
     */
    public List<Map<String,Long>> getCallTopByDateBetween(int top,Date d1,Date d2);

    /**
     * 时间范围内的话务量排名
     * top
     */
    public List<Map<String,Long>> getDurationTopByDateBetween(int top,Date d1,Date d2);
}
