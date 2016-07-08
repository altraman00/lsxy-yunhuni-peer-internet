package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.tenant.dao.TenantDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class TenantServiceImpl extends AbstractService<Tenant> implements TenantService {
    private static final String INCREASE_TID = "increase_tid";  //保存在redis里的自动增长的租户各识别码，达到9999后重新归0重新计

    @Autowired
    private AccountService accountService;

    @Autowired
    private TenantDao tenantDao;

    @Autowired
    private RedisCacheService cacheManager;

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

    @Override
    public Tenant createTenant() {
        //TODO redis上锁，确保不会发生超过9999的数字
        long incTid = cacheManager.incr(INCREASE_TID);
        Tenant tenant = new Tenant();
        tenant.setIsRealAuth(Tenant.AUTH_NO); //设为未实名认证状态
        tenant.setTenantUid(DateUtils.getTime("yyyyMMdd")+ incTid);
        if(incTid >= 9999){
            cacheManager.del(INCREASE_TID);
        }
        return this.save(tenant);
    }

}
