package com.lsxy.framework.tenant.service;

import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.tenant.dao.AccountDao;
import com.lsxy.framework.tenant.dao.BillingDao;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.model.Billing;
import com.lsxy.framework.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/28.
 */
@Service
public class BillingServiceImpl extends AbstractService<Billing> implements BillingService{
    @Autowired
    private BillingDao billingDao;

    @Autowired
    private AccountDao accountDao;

    @Override
    public BaseDaoInterface<Billing, Serializable> getDao() {
        return billingDao;
    }


    @Override
    public Billing getBiilingByUserName(String username) {
        Billing billing = null;
        Account account = accountDao.findByUserName(username);
        if(account != null){
            Tenant tenant = account.getTenant();
            if(tenant != null){
                billing = tenant.getBilling();
            }
        }
        return billing;
    }
}
