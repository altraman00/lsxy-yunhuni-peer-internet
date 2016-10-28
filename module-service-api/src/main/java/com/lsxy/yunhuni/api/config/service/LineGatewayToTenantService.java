package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;

import java.util.List;

/**
 * Created by liups on 2016/10/27.
 */
public interface LineGatewayToTenantService extends BaseService<LineGatewayToTenant> {
    /**
     * 获取租户的号码
     * @param tenantId
     * @return
     */
    List<LineGateway> findByTenantIdAndAreaId(String tenantId,String areaId);
}
