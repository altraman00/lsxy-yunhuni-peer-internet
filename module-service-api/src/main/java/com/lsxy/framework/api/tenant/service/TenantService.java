package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Tenant;

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
     * 获取注册的租户数量(deleted = false)
     * @return
     */
    int countValidTenant();

    /**
     * 获取本日注册的租户数量(deleted = false)
     * @return
     */
    int countValidTenantToday();

    /**
     * 获取本周注册的租户数量(deleted = false)
     * @return
     */
    int countValidTenantWeek();

    /**
     * 获取本月的租户数量(deleted = false)
     * @return
     */
    int countValidTenantMonth();

    /**
     * 获取已认证的租户数量(deleted = false)
     * @return
     */
    int countAuthTenant();

    /**
     * 获取本日已认证的租户数量(deleted = false)
     * @return
     */
    int countAuthTenantToday();

    /**
     * 获取本周已认证的租户数量(deleted = false)
     * @return
     */
    int countAuthTenantWeek();

    /**
     * 获取本月已认证的租户数量(deleted = false)
     * @return
     */
    int countAuthTenantMonth();

    /**
     * 获取已产生消费的租户数量
     * @return
     */
    int countConsumeTenant();

    /**
     * 获取本日已产生消费的租户数量
     * @return
     */
    int countConsumeTenantToday();

    /**
     * 获取本周已产生消费的租户数量
     * @return
     */
    int countConsumeTenantWeek();

    /**
     * 获取本月已产生消费的租户数量
     * @return
     */
    int countConsumeTenantMonth();
}
