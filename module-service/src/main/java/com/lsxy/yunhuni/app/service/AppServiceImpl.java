package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
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
    public List<App> findAppByUserName(String userName){
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from App obj where obj.tenant.id=?1 order by obj.status";
        List<App> list = this.findByCustomWithParams(hql, tenant.getId());

        return list;
    }

    @Override
    public Page<App> pageList(String userName, Integer pageNo, Integer pageSize) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from App obj where obj.tenant.id=?1 ";
        Page<App> page =  this.pageList(hql,pageNo,pageSize,tenant.getId());
        return page;
    }
}
