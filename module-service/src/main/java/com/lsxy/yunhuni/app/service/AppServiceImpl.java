package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.app.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
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

    @Autowired
    private ResourcesRentService resourcesRentService;

    @Autowired
    private ResourceTelenumService resourceTelenumService;

    @Override
    public BaseDaoInterface<App, Serializable> getDao() {
        return this.appDao;
    }

    @Override
    public long countByTenantIdAndName(String tenantId, String name) {
        return appDao.countByTenantIdAndName(tenantId,name);
    }

    @Override
    public List<App> findAppByUserName(String tenantId){
        String hql = "from App obj where obj.tenant.id=?1 order by obj.status";
        List<App> list = this.findByCustomWithParams(hql, tenantId);

        return list;
    }

    @Override
    public List<App> findAppByUserNameAndServiceType(String tenantId, String serviceType) {
        String hql = "from App obj where obj.tenant.id=?1 and obj.serviceType=?2 order by obj.status";
        List<App> list = this.findByCustomWithParams(hql, tenantId,serviceType);

        return list;
    }

    @Override
    public Page<App> pageList(String tenantId, Integer pageNo, Integer pageSize) {
        String hql = "from App obj where obj.tenant.id=?1 ";
        Page<App> page =  this.pageList(hql,pageNo,pageSize,tenantId);
        return page;
    }

    @Override
    public boolean isAppBelongToUser(String userName, String appId) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        App app = appDao.findOne(appId);
        if(app != null && tenant != null){
            return app.getTenant().getId().equals(tenant.getId());
        }else{
            return false;
        }
    }

    @Override
    public long countOnline() {
        return this.countByCustom("from App obj where obj.status = ?1",App.STATUS_ONLINE);
    }

    @Override
    public long countValid() {
        return this.countByCustom("from App obj");
    }

    @Override
    public int countValidDateBetween(Date d1, Date d2) {
        if(d1 == null){
            throw new NullPointerException();
        }
        if(d2 == null){
            throw new NullPointerException();
        }
        return appDao.countByDeletedAndCreateTimeBetween(Boolean.FALSE,d1,d2);
    }

    @Override
    public List<App> getAppsByTenantId(String tenantId) {
        if(tenantId == null){
            throw new IllegalArgumentException();
        }
        String hql = "from App obj where obj.tenant.id=?1 order by obj.status";
        List<App> list = this.findByCustomWithParams(hql, tenantId);
        return list;
    }

    @Override
    public String findOneAvailableTelnumber(App app) {
        if(app.getIsIvrService()==1){
            List<ResourcesRent> resourcesRents = resourcesRentService.findByAppId(app.getId());
            ResourcesRent resourcesRent = resourcesRents.get(0);
            return resourcesRent.getResData();
        }else{
            return resourceTelenumService.findOneFreeNumber(app.getArea().getId());
        }
    }


}
