package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.RealnamePrivate;

public interface RealnamePrivateService extends BaseService<RealnamePrivate> {

    /**
     * 根据租户id查找认证信息
     * @param tenantId
     * @return
     */
    public RealnamePrivate findByTenantId(String tenantId) ;
}
