package com.lsxy.app.oc.rest.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class StatisticDTO implements Serializable {

    @JsonProperty("app_count")
    private List<Integer> appCount;

    @JsonProperty("member_count")
    private List<Integer> memberCount;

    public List<Integer> getAppCount() {
        return appCount;
    }

    public void setAppCount(List<Integer> appCount) {
        this.appCount = appCount;
    }

    public List<Integer> getMemberCount() {
        return memberCount;
    }

    public void setMemberCount(List<Integer> memberCount) {
        this.memberCount = memberCount;
    }
}
