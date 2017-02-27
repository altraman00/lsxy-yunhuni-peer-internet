package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2016/11/14.
 */
public class ConditionCreateInputDTO extends CommonDTO {

    @NotNull
    @Size(max = 32)
    @JsonProperty("channel")
    private String channelId;

    @NotNull
    @Size(max = 512)
    @JsonProperty("where")
    private String whereExpression;

    @Size(max = 512)
    @JsonProperty("sort")
    private String sortExpression;

    @Min(value = 0)
    @Max(value = 99)
    @JsonProperty("priority")
    private Integer priority;

    @NotNull
    @Min(value = 1)
    @Max(value = 1000)
    @JsonProperty("queue_timeout")
    private Integer queueTimeout;

    @Min(value = 1)
    @Max(value = 60)
    @JsonProperty("fetch_timeout")
    private Integer fetchTimeout;

    @JsonProperty("remark")
    @Size(max = 128)
    private String remark;


    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getWhereExpression() {
        return whereExpression;
    }

    public void setWhereExpression(String whereExpression) {
        this.whereExpression = whereExpression;
    }

    public String getSortExpression() {
        return sortExpression;
    }

    public void setSortExpression(String sortExpression) {
        this.sortExpression = sortExpression;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getQueueTimeout() {
        return queueTimeout;
    }

    public void setQueueTimeout(Integer queueTimeout) {
        this.queueTimeout = queueTimeout;
    }

    public Integer getFetchTimeout() {
        return fetchTimeout;
    }

    public void setFetchTimeout(Integer fetchTimeout) {
        this.fetchTimeout = fetchTimeout;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
