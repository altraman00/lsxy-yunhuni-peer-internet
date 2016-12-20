package com.lsxy.yunhuni.api.config.model;

import com.lsxy.framework.api.base.IdEntity;
import org.hibernate.annotations.Where;

import javax.persistence.*;

/**
 * Created by liups on 2016/10/27.
 */
@Entity
@Where(clause = "deleted=0")
@Table(schema="db_lsxy_bi_yunhuni",name = "tb_oc_linegateway_to_tenant")
public class LineGatewayToTenant extends IdEntity {
    private LineGateway lineGateway;//线路网关
    private Integer priority;//优先级
    private String tenantId;

    @ManyToOne
    @JoinColumn(name = "line_id")
    public LineGateway getLineGateway() {
        return lineGateway;
    }

    public void setLineGateway(LineGateway lineGateway) {
        this.lineGateway = lineGateway;
    }

    @Column(name = "priority")
    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Column(name = "tenant_id")
    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
