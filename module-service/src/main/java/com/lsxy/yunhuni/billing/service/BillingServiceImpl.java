package com.lsxy.yunhuni.billing.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.billing.dao.BillingDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liups on 2016/6/28.
 */
@Service
public class BillingServiceImpl extends AbstractService<Billing> implements BillingService {
    @Autowired
    private BillingDao billingDao;

    @Autowired
    private TenantService tenantService;

    @Override
    public BaseDaoInterface<Billing, Serializable> getDao() {
        return billingDao;
    }
    @Override
    public Billing findBillingByUserName(String username){
        Billing billing = null;
        Tenant tenant = tenantService.findTenantByUserName(username);
        if(tenant != null){
            billing = findBillingByTenantId(tenant.getId());
        }
        return billing;
    }

    @Override
    public Billing findBillingByTenantId(String tenantId) {
        String hql = "from Billing obj where obj.tenant.id=?1";
        try {
            return this.findUnique(hql,tenantId);
        } catch (MatchMutiEntitiesException e) {
            e.printStackTrace();
            throw new RuntimeException("存在多个对应账务信息");
        }
    }

    @Override
    public BigDecimal getBalance(String tenantId) {
        Billing billing = this.findBillingByTenantId(tenantId);
        return billing.getBalance();
    }
}
