package com.lsxy.yunhuni.config.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/10/27.
 */
public interface LineGatewayToTenantDao extends BaseDaoInterface<LineGatewayToTenant, Serializable> {
    List<LineGatewayToTenant> findByTenantIdAndLineGateway_AreaId(String tenantId, String areaId);

    List<LineGatewayToTenant> findByTenantId(String tenantId);
}
