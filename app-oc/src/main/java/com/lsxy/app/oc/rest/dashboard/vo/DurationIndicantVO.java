package com.lsxy.app.oc.rest.dashboard.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/10.
 */
public class DurationIndicantVO implements Serializable{

    @JsonProperty("p_day")
    private Long pDay;

    @JsonProperty("pp_day")
    private Long ppDay;

    @JsonProperty("p_week")
    private Long pWeek;

    @JsonProperty("pp_week")
    private Long ppWeek;

    @JsonProperty("p_month")
    private Long pMonth;

    @JsonProperty("pp_month")
    private Long ppMonth;

    public Long getpDay() {
        if(pDay == null){
            pDay = 0L;
        }
        return (long)Math.round(pDay/60);
    }

    public void setpDay(Long pDay) {
        this.pDay = pDay;
    }

    public Long getPpDay() {
        if(ppDay == null){
            ppDay = 0L;
        }
        return (long)Math.round(ppDay/60);
    }

    public void setPpDay(Long ppDay) {
        this.ppDay = ppDay;
    }

    public Long getpWeek() {
        if(pWeek == null){
            pWeek = 0L;
        }
        return (long)Math.round(pWeek/60);
    }

    public void setpWeek(Long pWeek) {
        this.pWeek = pWeek;
    }

    public Long getPpWeek() {
        if(ppWeek == null){
            ppWeek = 0L;
        }
        return (long)Math.round(ppWeek/60);
    }

    public void setPpWeek(Long ppWeek) {
        this.ppWeek = ppWeek;
    }

    public Long getpMonth() {
        if(pMonth == null){
            pMonth = 0L;
        }
        return (long)Math.round(pMonth/60);
    }

    public void setpMonth(Long pMonth) {
        this.pMonth = pMonth;
    }

    public Long getPpMonth() {
        if(ppMonth == null){
            ppMonth = 0L;
        }
        return (long)Math.round(ppMonth/60);
    }

    public void setPpMonth(Long ppMonth) {
        this.ppMonth = ppMonth;
    }
}
