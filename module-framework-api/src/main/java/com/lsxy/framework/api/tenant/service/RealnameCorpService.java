package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.RealnameCorp;

public interface RealnameCorpService extends BaseService<RealnameCorp> {
    /**
     * 根据租户id查找认证信息
     * @param tenantId
     * @return
     */
    public RealnameCorp findByTenantId(String tenantId) ;
}
