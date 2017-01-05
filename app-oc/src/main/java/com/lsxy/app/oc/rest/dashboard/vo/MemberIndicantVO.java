package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/9.
 */
public class MemberIndicantVO implements Serializable{

    @JsonProperty("regist_total")
    private Integer registTotal;

    @JsonProperty("regist_tatal_day")
    private Integer registTatalDay;

    @JsonProperty("regist_tatal_week")
    private Integer registTatalWeek;

    @JsonProperty("regist_tatal_month")
    private Integer registTatalMonth;

    @JsonProperty("auth_total")
    private Integer authTotal;

    @JsonProperty("auth_total_day")
    private Integer authTotalDay;

    @JsonProperty("auth_total_week")
    private Integer authTotalWeek;

    @JsonProperty("auth_total_month")
    private Integer authTotalMonth;

    @JsonProperty("consume")
    private Integer consume;

    @JsonProperty("consume_day")
    private Integer consumeDay;

    @JsonProperty("consume_week")
    private Integer consumeWeek;

    @JsonProperty("consume_month")
    private Integer consumeMonth;

    public MemberIndicantVO(){

    }

    public Integer getRegistTotal() {
        return registTotal;
    }

    public void setRegistTotal(Integer registTotal) {
        this.registTotal = registTotal;
    }

    public Integer getRegistTatalDay() {
        return registTatalDay;
    }

    public void setRegistTatalDay(Integer registTatalDay) {
        this.registTatalDay = registTatalDay;
    }

    public Integer getRegistTatalWeek() {
        return registTatalWeek;
    }

    public void setRegistTatalWeek(Integer registTatalWeek) {
        this.registTatalWeek = registTatalWeek;
    }

    public Integer getRegistTatalMonth() {
        return registTatalMonth;
    }

    public void setRegistTatalMonth(Integer registTatalMonth) {
        this.registTatalMonth = registTatalMonth;
    }

    public Integer getAuthTotal() {
        return authTotal;
    }

    public void setAuthTotal(Integer authTotal) {
        this.authTotal = authTotal;
    }

    public Integer getAuthTotalDay() {
        return authTotalDay;
    }

    public void setAuthTotalDay(Integer authTotalDay) {
        this.authTotalDay = authTotalDay;
    }

    public Integer getAuthTotalWeek() {
        return authTotalWeek;
    }

    public void setAuthTotalWeek(Integer authTotalWeek) {
        this.authTotalWeek = authTotalWeek;
    }

    public Integer getAuthTotalMonth() {
        return authTotalMonth;
    }

    public void setAuthTotalMonth(Integer authTotalMonth) {
        this.authTotalMonth = authTotalMonth;
    }

    public Integer getConsume() {
        return consume;
    }

    public void setConsume(Integer consume) {
        this.consume = consume;
    }

    public Integer getConsumeDay() {
        return consumeDay;
    }

    public void setConsumeDay(Integer consumeDay) {
        this.consumeDay = consumeDay;
    }

    public Integer getConsumeWeek() {
        return consumeWeek;
    }

    public void setConsumeWeek(Integer consumeWeek) {
        this.consumeWeek = consumeWeek;
    }

    public Integer getConsumeMonth() {
        return consumeMonth;
    }

    public void setConsumeMonth(Integer consumeMonth) {
        this.consumeMonth = consumeMonth;
    }
}
