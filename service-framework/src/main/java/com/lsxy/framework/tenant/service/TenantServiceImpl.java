package com.lsxy.framework.tenant.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.tenant.dao.TenantDao;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.model.Tenant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class TenantServiceImpl extends AbstractService<Tenant> implements TenantService{
    @Autowired
    private AccountService accountService;

    @Autowired
    private TenantDao tenantDao;

    @Override
    public BaseDaoInterface<Tenant, Serializable> getDao() {
        return this.tenantDao;
    }

    @Override
    public Tenant findTenantByUserName(String userName) throws MatchMutiEntitiesException {
        Tenant tenant = null;
        Account account = accountService.findAccountByUserName(userName);
        if(account != null){
            tenant = account.getTenant();
        }
        return tenant;
    }
}
