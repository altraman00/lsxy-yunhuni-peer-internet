package com.lsxy.app.portal.console.statistics;

/**
 * Created by zhangxb on 2016/7/6.
 */
public class ConsumeStatisticsVo {
    private String type;//类型 month year
    private String startTime;//时间
    private String endTime;//比较时间
    private String appId;//应用编号

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
}
