package com.lsxy.call.center.api.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by liuws on 2016/11/9.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_bi_call_center_condition")
public class Condition extends IdEntity{

    private String tenantId;

    private String appId;

    private String channelId;

    private String whereExpression;

    private String sortExpression;

    private Integer priority;

    private Integer queueTimeout;

    private Integer fetchTimeout;

    private String remark;

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "app_id")
    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    @Column(name = "channel_id")
    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    @Column(name="where_expression")
    public String getWhereExpression() {
        return whereExpression;
    }

    public void setWhereExpression(String whereExpression) {
        this.whereExpression = whereExpression;
    }

    @Column(name="sort_expression")
    public String getSortExpression() {
        return sortExpression;
    }

    public void setSortExpression(String sortExpression) {
        this.sortExpression = sortExpression;
    }

    @Column(name="priority")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name="queue_timeout")
    public Integer getQueueTimeout() {
        return queueTimeout;
    }

    public void setQueueTimeout(Integer queueTimeout) {
        this.queueTimeout = queueTimeout;
    }

    @Column(name="fetch_timeout")
    public Integer getFetchTimeout() {
        return fetchTimeout;
    }

    public void setFetchTimeout(Integer fetchTimeout) {
        this.fetchTimeout = fetchTimeout;
    }

    @Column(name="remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
