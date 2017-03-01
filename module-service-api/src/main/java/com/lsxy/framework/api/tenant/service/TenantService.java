package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.model.TenantVO;
import com.lsxy.framework.core.utils.Page;

import java.util.*;

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
     * @param account 创建租户的用户
     * @return
     */
    Tenant createTenant(Account account);

    /**
     * 获取注册的租户数量(deleted = false)
     * @return
     */
    int countValidTenant();

    /**
     * 获取时间范围内注册的租户数量(deleted = false)
     * @return
     */
    int countValidTenantDateBetween(Date d1, Date d2);
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

    Page<TenantVO> pageListBySearch(String name,Date regDateStart,Date regDateEnd,
                                    Integer authStatus,Integer accStatus,int pageNo,int pageSize);



    /**
     * 根据会员名模糊查找Tenant
     * @param name 会员名字
     * @return
     */
    List<Tenant>  pageListByUserName(String name);

    /**
     * 获取未处理的记录数
     * @return
     */
    Map getAwaitNum();

    List<Tenant> findByIds(Collection<String> ids);

    Page<TenantVO> pageListBySearchAndAccount(String name,Date regDateStart,Date regDateEnd,
                                    Integer authStatus,Integer accStatus,int pageNo,int pageSize);
}
