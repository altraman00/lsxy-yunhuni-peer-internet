package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;

import java.util.List;

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
     */
    Tenant findTenantByUserName(String userName);

    /**
     * 创建租户
     * @return
     */
    Tenant createTenant();

    /**
     * 根据会员名模糊查找Tenant
     * @param name
     * @return
     */
    List<Tenant>  pageListByUserName(String name);
}
