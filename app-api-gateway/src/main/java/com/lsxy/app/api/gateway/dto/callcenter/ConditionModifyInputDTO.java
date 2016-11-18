package com.lsxy.app.api.gateway.dto.callcenter;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lsxy.app.api.gateway.dto.CommonDTO;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by liuws on 2016/11/14.
 */
public class ConditionModifyInputDTO extends CommonDTO {

    @Size(max = 512)
    private String whereExpression;

    @Size(max = 512)
    private String sortExpression;

    @Min(value = 0)
    private Integer priority;

    @Min(value = 1)
    @Max(value = 1000)
    private Integer queueTimeout;

    @Min(value = 0)
    private Integer fetchTimeout;

    @JsonProperty("remark")
    @Size(max = 128)
    private String remark;


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
