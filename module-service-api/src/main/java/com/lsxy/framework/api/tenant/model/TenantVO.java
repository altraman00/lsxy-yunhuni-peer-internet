package com.lsxy.framework.api.tenant.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by liuws on 2016/8/10.
 */
@Entity()
@SqlResultSetMapping(name="tenantResult",
entities= {
    @EntityResult(entityClass=TenantVO.class,
        fields= {
            @FieldResult(name="id", column="id"),
            @FieldResult(name="title", column="title"),
            @FieldResult(name="created", column="created"),
            @FieldResult(name="text", column="text"),
            @FieldResult(name="cat_name", column="cat_name"),
            @FieldResult(name="author", column="author")
        })
    }
)
public class TenantVO implements Serializable {

    @JsonProperty("uid")
    @Id
    private String id;

    @JsonProperty("registe_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(Integer authStatus) {
        this.authStatus = authStatus;
    }

    public Integer getAppCount() {
        return appCount;
    }

    public void setAppCount(Integer appCount) {
        this.appCount = appCount;
    }

    public Double getRemainCoin() {
        return remainCoin;
    }

    public void setRemainCoin(Double remainCoin) {
        this.remainCoin = remainCoin;
    }

    public Double getCostCoin() {
        return costCoin;
    }

    public void setCostCoin(Double costCoin) {
        this.costCoin = costCoin;
    }

    public Double getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(Double totalCoin) {
        this.totalCoin = totalCoin;
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

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }
}
