package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/10.
 */
public class ConsumeIndicantVO implements Serializable{

    @JsonProperty("consume_day")
    private Double consume;

    @JsonProperty("day_rate")
    private Double rateOfDay;

    @JsonProperty("week_rate")
    private Double rateOfWeek;

    @JsonProperty("month_rate")
    private Double rateOfMonth;

    public Double getConsume() {
        return consume;
    }

    public void setConsume(Double consume) {
        this.consume = consume;
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
