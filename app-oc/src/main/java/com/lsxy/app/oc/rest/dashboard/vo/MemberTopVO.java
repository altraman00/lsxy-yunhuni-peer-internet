package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by liuws on 2016/8/10.
 */
public class MemberTopVO implements Serializable{

    @JsonProperty("call_top")
    private List<Map<String, Object>> callTop;

    @JsonProperty("duration_top")
    private List<Map<String, Object>> durationTop;

    @JsonProperty("consume_top")
    private List<Map<String, Object>> consumeTop;

    @JsonProperty("duration_week_top")
    private List<Map<String, Object>> durationWeekTop;

    @JsonProperty("call_week_top")
    private List<Map<String, Object>> callWeekTop;

    public List<Map<String, Object>> getDurationTop() {
        return durationTop;
    }

    public void setDurationTop(List<Map<String, Object>> durationTop) {
        this.durationTop = durationTop;
    }

    public List<Map<String, Object>> getCallTop() {
        return callTop;
    }

    public void setCallTop(List<Map<String, Object>> callTop) {
        this.callTop = callTop;
    }

    public List<Map<String, Object>> getConsumeTop() {
        return consumeTop;
    }

    public void setConsumeTop(List<Map<String, Object>> consumeTop) {
        this.consumeTop = consumeTop;
    }

    public List<Map<String, Object>> getDurationWeekTop() {
        return durationWeekTop;
    }

    public void setDurationWeekTop(List<Map<String, Object>> durationWeekTop) {
        this.durationWeekTop = durationWeekTop;
    }

    public List<Map<String, Object>> getCallWeekTop() {
        return callWeekTop;
    }

    public void setCallWeekTop(List<Map<String, Object>> callWeekTop) {
        this.callWeekTop = callWeekTop;
    }
}
