package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;

import java.util.List;

public interface RealnamePrivateService extends BaseService<RealnamePrivate> {

    /**
     * 根据租户id查找认证信息
     * @param tenantId 租户信息
     * @return
     */
    public List<RealnamePrivate> findByTenantId(String tenantId) ;

    /**
     * 根据组合id和状态查找认证信息
     * @param tenantId 租户id
     * @param status 状态
     * @return
     */
    public RealnamePrivate findByTenantIdAndStatus(String tenantId,int status);

    public RealnamePrivate findByTenantIdNewest(String tenantId);
}
