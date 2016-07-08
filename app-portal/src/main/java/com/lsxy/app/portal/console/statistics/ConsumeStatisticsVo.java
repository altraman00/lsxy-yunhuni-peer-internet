package com.lsxy.app.portal.console.statistics;

import com.lsxy.framework.core.utils.DateUtils;

import java.time.Month;
import java.util.Date;

/**
 * Created by zhangxb on 2016/7/6.
 */
public class ConsumeStatisticsVo {
    public static final String TYPE_MONTH = "month";//日统计类型 按月查找输出 返回按月
    public static final String TYPE_YEAR = "year";//月统计类型 按年查找输出 返回按年
    private String type = TYPE_MONTH;//类型统计类型 month year
    private String startTime = DateUtils.formatDate(new Date(),"yyyy-MM");//时间
    private String endTime = "";//比较时间
    private String startTimeYear = DateUtils.formatDate(new Date(),"yyyy");//时间
    private String endTimeYear = "";//比较时间
    private String appId = "0";//应用编号

    public ConsumeStatisticsVo() {
    }

    public ConsumeStatisticsVo(String type, String startTime, String endTime, String appId) {
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.appId = appId;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getStartTimeYear() {
        return startTimeYear;
    }

    public void setStartTimeYear(String startTimeYear) {
        this.startTimeYear = startTimeYear;
    }

    public String getEndTimeYear() {
        return endTimeYear;
    }

    public void setEndTimeYear(String endTimeYear) {
        this.endTimeYear = endTimeYear;
    }
}
