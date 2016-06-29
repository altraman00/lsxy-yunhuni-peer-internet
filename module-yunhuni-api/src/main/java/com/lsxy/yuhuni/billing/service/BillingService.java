package com.lsxy.yuhuni.billing.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.service.BaseService;
import com.lsxy.yuhuni.billing.model.Billing;

/**
 * Created by liups on 2016/6/28.
 */
public interface BillingService extends BaseService<Billing> {
    /**
     * 根据用户名查找账务
     * @param username 用户名
     * @return
     */
    Billing findBillingByUserName(String username) throws MatchMutiEntitiesException;

    /**
     * 根据租户ID查找账务
     * @param tenantId 租户ID
     * @return
     */
    Billing findBillingByTenantId(String tenantId) throws MatchMutiEntitiesException;
}
