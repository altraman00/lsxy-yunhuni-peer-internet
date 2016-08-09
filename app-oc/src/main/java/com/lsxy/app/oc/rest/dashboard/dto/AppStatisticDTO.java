package com.lsxy.app.oc.rest.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/9.
 */
public class AppStatisticDTO implements Serializable {

    @JsonProperty("app_count")
    private List<Integer> appCount;

    public List<Integer> getAppCount() {
        return appCount;
    }

    public void setAppCount(List<Integer> appCount) {
        this.appCount = appCount;
    }
}
