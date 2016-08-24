package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.TenantServiceSwitch;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;

/**
 * 租户功能开关
 * 
 * @author Liuws
 *
 */
@SuppressWarnings("rawtypes")
public interface TenantServiceSwitchService extends BaseService<TenantServiceSwitch> {
    public TenantServiceSwitch findOneByTenant(String tenant) throws MatchMutiEntitiesException;

    public TenantServiceSwitch saveOrUpdate(String tenant,TenantServiceSwitch switchs);
}
