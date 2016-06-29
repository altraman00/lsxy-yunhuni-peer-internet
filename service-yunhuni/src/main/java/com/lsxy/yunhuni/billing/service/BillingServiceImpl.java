package com.lsxy.yunhuni.billing.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.model.Tenant;
import com.lsxy.framework.tenant.service.AccountService;
import com.lsxy.framework.tenant.service.TenantService;
import com.lsxy.yuhuni.billing.model.Billing;
import com.lsxy.yuhuni.billing.service.BillingService;
import com.lsxy.yunhuni.billing.dao.BillingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/28.
 */
@Service
public class BillingServiceImpl extends AbstractService<Billing> implements BillingService {
    @Autowired
    private BillingDao billingDao;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TenantService tenantService;

    @Override
    public BaseDaoInterface<Billing, Serializable> getDao() {
        return billingDao;
    }


    @Override
    public Billing findBillingByUserName(String username) throws MatchMutiEntitiesException {
        Billing billing = null;
        Tenant tenant = tenantService.findTenantByUserName(username);
        if(tenant != null){
            billing = findBillingByTenantId(tenant.getId());
        }
        return billing;
    }

    @Override
    public Billing findBillingByTenantId(String tenantId) throws MatchMutiEntitiesException {
        String hql = "from Billing obj where obj.tenant.id=?1";
        return this.findUnique(hql,tenantId);
    }
}
