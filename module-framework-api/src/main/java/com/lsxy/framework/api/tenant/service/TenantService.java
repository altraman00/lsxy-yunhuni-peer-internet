package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;

/**
 * 租户Mananger
 * 
 * @author WangYun
 *
 */
@SuppressWarnings("rawtypes")
public interface TenantService extends BaseService<Tenant> {
    /**
     * @param userName 用户名
     * @return 租户信息
     * @throws MatchMutiEntitiesException
     */
    Tenant findTenantByUserName(String userName) throws MatchMutiEntitiesException;

    /**
     * 创建租户
     * @return
     */
    Tenant createTenant();
}
