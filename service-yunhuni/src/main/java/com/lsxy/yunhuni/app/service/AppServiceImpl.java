package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.persistence.BaseDaoInterface;
import com.lsxy.framework.core.service.AbstractService;
import com.lsxy.framework.tenant.model.Tenant;
import com.lsxy.framework.tenant.service.TenantService;
import com.lsxy.yuhuni.apicertificate.model.ApiCertificate;
import com.lsxy.yuhuni.app.model.App;
import com.lsxy.yuhuni.app.service.AppService;
import com.lsxy.yunhuni.app.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class AppServiceImpl extends AbstractService<App> implements AppService {
    @Autowired
    private AppDao appDao;

    @Autowired
    private TenantService tenantService;

    @Override
    public BaseDaoInterface<App, Serializable> getDao() {
        return this.appDao;
    }

    @Override
    public List<App> findAppByUserName(String userName) throws MatchMutiEntitiesException {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from App obj where obj.tenant.id=?1";
        List<App> list = this.findByCustomWithParams(hql, tenant.getId());

        return list;
    }



}
