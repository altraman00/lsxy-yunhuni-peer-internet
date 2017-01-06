package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/10.
 */
public class DurationIndicantVO implements Serializable{

    @JsonProperty("duration_day")
    private Long duration;

    @JsonProperty("day_rate")
    private Double rateOfDay;

    @JsonProperty("week_rate")
    private Double rateOfWeek;

    @JsonProperty("month_rate")
    private Double rateOfMonth;


    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Double getRateOfDay() {
        return rateOfDay;
    }

    public void setRateOfDay(Double rateOfDay) {
        this.rateOfDay = rateOfDay;
    }

    public Double getRateOfWeek() {
        return rateOfWeek;
    }

    public void setRateOfWeek(Double rateOfWeek) {
        this.rateOfWeek = rateOfWeek;
    }

    public Double getRateOfMonth() {
        return rateOfMonth;
    }

    public void setRateOfMonth(Double rateOfMonth) {
        this.rateOfMonth = rateOfMonth;
    }
}
