package com.lsxy.framework.api.tenant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by liuws on 2016/8/10.
 */
public class TenantVO implements Serializable {

    @JsonProperty("uid")
    private String id;

    @JsonProperty("registe_date")
    private Date regDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("auth_status")
    private Integer authStatus;

    @JsonProperty("app_count")
    private Integer appCount;

    @JsonProperty("remain_coin")
    private Double remainCoin;

    @JsonProperty("cost_coin")
    private Double costCoin;

    @JsonProperty("total_coin")
    private Double totalCoin;

    @JsonProperty("session_count")
    private Long sessionCount;

    @JsonProperty("session_time")
    private Long sessionTime;

    @JsonProperty("account_status")
    private Integer accountStatus;

}
