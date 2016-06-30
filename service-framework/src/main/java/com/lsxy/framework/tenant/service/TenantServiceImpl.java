package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
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
    @Autowired
    private TenantDao tenantDao;

    @Override
    public BaseDaoInterface<Tenant, Serializable> getDao() {
        return tenantDao;
    }




}
