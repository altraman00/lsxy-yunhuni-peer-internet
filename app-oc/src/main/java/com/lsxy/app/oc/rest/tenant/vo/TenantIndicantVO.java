package com.lsxy.app.oc.rest.tenant.vo;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * Created by liuws on 2016/8/11.
 */
public class TenantIndicantVO implements Serializable{

    @JsonProperty("cost_coin")
    private Double costCoin;

    @JsonProperty("recharge_coin")
    private Double rechargeCoin;

    @JsonProperty("session_count")
    private Long sessionCount;

    @JsonProperty("session_time")
    private Long sessionTime;

    @JsonProperty("age_session_time")
    private Long avgSessionTime;

    @JsonProperty("connected_rate")
    private Double connectedRate;


    @JsonProperty("cost_coin_rate")
    private Double costCoinRate;

    @JsonProperty("recharge_coin_rate")
    private Double rechargeCoinRate;

    @JsonProperty("session_count_rate")
    private Double sessionCountRate;

    @JsonProperty("session_time_rate")
    private Double sessionTimeRate;

    @JsonProperty("age_session_time_rate")
    private Double avgSessionTimeRate;

    @JsonProperty("connected_rate_rate")
    private Double connectedRateRate;

    public Double getCostCoin() {
        return costCoin;
    }

    public void setCostCoin(Double costCoin) {
        this.costCoin = costCoin;
    }

    public Double getRechargeCoin() {
        return rechargeCoin;
    }

    public void setRechargeCoin(Double rechargeCoin) {
        this.rechargeCoin = rechargeCoin;
    }

    public Long getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Long sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Long getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(Long sessionTime) {
        this.sessionTime = sessionTime;
    }

    public Long getAvgSessionTime() {
        return avgSessionTime;
    }

    public void setAvgSessionTime(Long avgSessionTime) {
        this.avgSessionTime = avgSessionTime;
    }

    public Double getConnectedRate() {
        return connectedRate;
    }

    public void setConnectedRate(Double connectedRate) {
        this.connectedRate = connectedRate;
    }

    public Double getCostCoinRate() {
        return costCoinRate;
    }

    public void setCostCoinRate(Double costCoinRate) {
        this.costCoinRate = costCoinRate;
    }

    public Double getRechargeCoinRate() {
        return rechargeCoinRate;
    }

    public void setRechargeCoinRate(Double rechargeCoinRate) {
        this.rechargeCoinRate = rechargeCoinRate;
    }

    public Double getSessionCountRate() {
        return sessionCountRate;
    }

    public void setSessionCountRate(Double sessionCountRate) {
        this.sessionCountRate = sessionCountRate;
    }

    public Double getSessionTimeRate() {
        return sessionTimeRate;
    }

    public void setSessionTimeRate(Double sessionTimeRate) {
        this.sessionTimeRate = sessionTimeRate;
    }

    public Double getAvgSessionTimeRate() {
        return avgSessionTimeRate;
    }

    public void setAvgSessionTimeRate(Double avgSessionTimeRate) {
        this.avgSessionTimeRate = avgSessionTimeRate;
    }

    public Double getConnectedRateRate() {
        return connectedRateRate;
    }

    public void setConnectedRateRate(Double connectedRateRate) {
        this.connectedRateRate = connectedRateRate;
    }
}
