package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.RealnameCorp;

import java.util.List;

public interface RealnameCorpService extends BaseService<RealnameCorp> {
    /**
     * 根据租户id查找认证信息
     * @param tenantId 租户id
     * @return
     */
    public List<RealnameCorp> findByTenantId(String tenantId) ;

    /**
     * 根据租户id和状态查找
     * @param tenantId 租户id
     * @param status 状态
     * @return
     */
    public RealnameCorp findByTenantIdAndStatus(String tenantId,int status);

    public RealnameCorp findByTenantIdNewest(String tenantId);
}
