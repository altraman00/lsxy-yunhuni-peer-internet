package com.lsxy.framework.tenant.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.service.BaseService;
import com.lsxy.framework.tenant.model.Tenant;

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

}
