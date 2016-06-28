package com.lsxy.framework.tenant.service;

import com.lsxy.framework.core.service.BaseService;
import com.lsxy.framework.tenant.model.Billing;

/**
 * Created by liups on 2016/6/28.
 */
public interface BillingService extends BaseService<Billing> {
    Billing getBiilingByUserName(String username);

}
