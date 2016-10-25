package com.lsxy.framework.api.tenant.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liuws on 2016/8/10.
 */
@Entity()
@SqlResultSetMapping(name="tenantResult",
entities= {
    @EntityResult(
        entityClass=TenantVO.class,
        fields= {
            @FieldResult(name="id", column="id"),
            @FieldResult(name="name", column="name"),
            @FieldResult(name="regDate", column="regDate"),
            @FieldResult(name="authStatus", column="authStatus"),
            @FieldResult(name="accountStatus", column="accountStatus"),
            @FieldResult(name="appCount", column="appCount")
        })
    }
)
public class TenantVO implements Serializable {

    @JsonProperty("uid")
    @Id
    private String id;

    @JsonProperty("registe_date")
    //@JsonFormat(timezone="GMT+8",pattern = "yyyy-MM-dd HH:mm")
    private Date regDate;

    @JsonProperty("name")
    private String name;

    @JsonProperty("auth_status")
    private Integer authStatus;

    @JsonProperty("app_count")
    private Integer appCount;

    @JsonProperty("remain_coin")
    @Transient
    private Double remainCoin;

    @JsonProperty("cost_coin")
    @Transient
    private BigDecimal costCoin;

    @JsonProperty("total_coin")
    @Transient
    private BigDecimal totalCoin;

    @JsonProperty("session_count")
    @Transient
    private Long sessionCount;

    @JsonProperty("session_time")
    @Transient
    private Long sessionTime;

    @JsonProperty("account_status")
    private Integer accountStatus;

//    @Transient
//    private boolean inited = false;

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
        if(authStatus!=null){
            for (int i : Tenant.AUTH_STATUS) {
                if(i == authStatus){
                    return 1;
                }
            }
        }
        return 0;
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

    public BigDecimal getCostCoin() {
        return costCoin;
    }

    public void setCostCoin(BigDecimal costCoin) {
        this.costCoin = costCoin;
    }

    public BigDecimal getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(BigDecimal totalCoin) {
        this.totalCoin = totalCoin;
    }

    public Long getSessionCount() {
        return sessionCount;
    }

    public void setSessionCount(Long sessionCount) {
        this.sessionCount = sessionCount;
    }

    public Long getSessionTime() {//秒转换成分钟
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
