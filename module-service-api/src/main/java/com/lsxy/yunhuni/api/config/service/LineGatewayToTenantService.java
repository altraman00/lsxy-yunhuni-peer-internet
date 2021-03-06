package com.lsxy.yunhuni.api.config.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.config.model.LineGateway;
import com.lsxy.yunhuni.api.config.model.LineGatewayToTenant;
import com.lsxy.yunhuni.api.config.model.LineGatewayVO;

import java.util.List;

/**
 * Created by liups on 2016/10/27.
 */
public interface LineGatewayToTenantService extends BaseService<LineGatewayToTenant> {
    /**
     * 获取租户的号码(专为选择呼出号码使用)
     * @param tenantId
     * @return
     */
    List<LineGatewayVO> findByTenantIdAndAreaId(String tenantId, String areaId);

    List<LineGateway> findLineGatewayByTenantId(String tenantId);

    Page<LineGatewayToTenant> getPage(String tenantId,Integer pageNo, Integer pageSize);
    long findByLineIdAndTenantId(String lineId,String tenantId);
    int getMaxPriority(String tenantId);
    void deleteLine(String line);

    int upPriority(int o1,int o2,String line);

    /**
     * 移除私有线路
     */
    void removeTenantLine(String id,String tenantId);

}
