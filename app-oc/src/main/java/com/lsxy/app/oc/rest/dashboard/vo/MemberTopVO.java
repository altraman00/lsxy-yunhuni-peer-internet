package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/8/10.
 */
public class MemberTopVO implements Serializable{

    @JsonProperty("duration_top")
    private List<Map<String, Long>> durationTop;

    @JsonProperty("call_top")
    private List<Map<String, Long>> callTop;

    @JsonProperty("consume_top")
    private List<Map<String, Double>> consumeTop;

    @JsonProperty("duration_week_top")
    private List<Map<String, Long>> durationWeekTop;

    @JsonProperty("call_week_top")
    private List<Map<String, Long>> callWeekTop;

    public List<Map<String, Long>> getDurationTop() {
        return durationTop;
    }

    public void setDurationTop(List<Map<String, Long>> durationTop) {
        this.durationTop = durationTop;
    }

    public List<Map<String, Long>> getCallTop() {
        return callTop;
    }

    public void setCallTop(List<Map<String, Long>> callTop) {
        this.callTop = callTop;
    }

    public List<Map<String, Double>> getConsumeTop() {
        return consumeTop;
    }

    public void setConsumeTop(List<Map<String, Double>> consumeTop) {
        this.consumeTop = consumeTop;
    }

    public List<Map<String, Long>> getDurationWeekTop() {
        return durationWeekTop;
    }

    public void setDurationWeekTop(List<Map<String, Long>> durationWeekTop) {
        this.durationWeekTop = durationWeekTop;
    }

    public List<Map<String, Long>> getCallWeekTop() {
        return callWeekTop;
    }

    public void setCallWeekTop(List<Map<String, Long>> callWeekTop) {
        this.callWeekTop = callWeekTop;
    }
}
