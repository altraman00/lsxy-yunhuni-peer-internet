package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.model.TenantServiceSwitch;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.tenant.dao.TenantServiceSwitchDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhangxb on 2016/6/29.
 * 租户实现类
 */
@Service
public class TenantServiceSwitchServiceImpl extends AbstractService<TenantServiceSwitch> implements TenantServiceSwitchService {
    private static final String INCREASE_TID = "increase_tid";  //保存在redis里的自动增长的租户各识别码，达到9999后重新归0重新计

    @Autowired
    private TenantServiceSwitchDao tenantServiceSwitchDao;

    @Autowired
    private TenantService tenantService;

    @Override
    public BaseDaoInterface<TenantServiceSwitch, Serializable> getDao() {
        return this.tenantServiceSwitchDao;
    }


    @Override
    @Cacheable(value="entity",key="'entity_switch_'+#tenantId",unless = "#result == null")
    public TenantServiceSwitch findOneByTenant(String tenantId) throws MatchMutiEntitiesException {
        String hql = "from TenantServiceSwitch obj where obj.tenant.id = ?1";
        TenantServiceSwitch tenantServiceSwitch = this.findUnique(hql, tenantId);
        return tenantServiceSwitch;
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_switch_' + #tenantId", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "entity", key = "'entity_switch_' + #tenantId",unless = "#entity == null")
            }
    )
    public TenantServiceSwitch saveOrUpdate(String tenantId,TenantServiceSwitch switchs) {
        TenantServiceSwitch s = null;
        try{
            s = findOneByTenant(tenantId);
        }catch (Throwable e) {
            throw new RuntimeException(e);
        }
        if(s == null){
            s = new TenantServiceSwitch();
            Tenant tenant = tenantService.findById(tenantId);
            if(tenant == null){
                throw new NullPointerException();
            }
            s.setTenant(tenant);
        }
        if(switchs!=null){
            s.setIsIvrService(switchs.getIsIvrService() == null?0:switchs.getIsIvrService());
            s.setIsRecording(switchs.getIsRecording() == null?0:switchs.getIsRecording());
            s.setIsSessionService(switchs.getIsSessionService() == null?0:switchs.getIsSessionService());
            s.setIsVoiceCallback(switchs.getIsVoiceCallback() == null?0:switchs.getIsVoiceCallback());
            s.setIsVoiceDirectly(switchs.getIsVoiceDirectly() == null?0:switchs.getIsVoiceDirectly());
            s.setIsVoiceValidate(switchs.getIsVoiceValidate() == null?0:switchs.getIsVoiceValidate());
            s.setIsCallCenter(switchs.getIsCallCenter() == null?0:switchs.getIsCallCenter());
        }else{
            s.setIsIvrService(1);
            s.setIsRecording(1);
            s.setIsSessionService(1);
            s.setIsVoiceCallback(1);
            s.setIsVoiceDirectly(1);
            s.setIsVoiceValidate(1);
            s.setIsCallCenter(1);
        }
        return this.save(s);
    }
}
