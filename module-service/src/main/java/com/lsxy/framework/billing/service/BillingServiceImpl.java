package com.lsxy.framework.billing.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.billing.dao.BillingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/6/28.
 */
@Service
public class BillingServiceImpl extends AbstractService<Billing> implements BillingService {
    private static final Logger logger = LoggerFactory.getLogger(BillingServiceImpl.class);
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
    @Cacheable(value="billing",key = "'billing_'+#tenantId" ,unless="#result == null")
    public Billing findBillingByTenantId(String tenantId) {
        String hql = "from Billing obj where obj.tenantId=?1";
        try {
            return this.findUnique(hql,tenantId);
        } catch (MatchMutiEntitiesException e) {
            logger.error("存在多个对应账务信息",e);
            throw new RuntimeException("存在多个对应账务信息");
        }catch(Exception e){
            logger.error("获取账务信息异常",e);
            return null;
        }
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true),
                    @CacheEvict(value = "billing", key = "'billing_' + #entity.tenantId", beforeInvocation = true)
            }
    )
    @Override
    public Billing save(Billing entity) {
        return getDao().save(entity);
    }
}
