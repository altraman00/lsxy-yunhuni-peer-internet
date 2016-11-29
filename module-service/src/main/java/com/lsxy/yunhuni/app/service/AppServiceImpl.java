package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.model.TenantServiceSwitch;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.api.tenant.service.TenantServiceSwitchService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.app.service.ServiceType;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.model.AreaSip;
import com.lsxy.yunhuni.api.config.service.AreaSipService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.app.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class AppServiceImpl extends AbstractService<App> implements AppService {
    private static final String APP_CC_NUM_PREFIX5 = "APP_CC_NUM_PREFIX5";
    private static final String APP_CC_NUM_PREFIX6 = "APP_CC_NUM_PREFIX6";
    private static final String APP_CC_NUM_PREFIX7 = "APP_CC_NUM_PREFIX7";

    @Autowired
    private AppDao appDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ResourcesRentService resourcesRentService;
    @Autowired
    private ResourceTelenumService resourceTelenumService;
    @Autowired
    private AreaSipService areaSipService;
    @Autowired
    private RedisCacheService redisCacheService;

    @Autowired
    private TenantServiceSwitchService tenantServiceSwitchService;

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
    public App create(App app) {
        if(App.PRODUCT_CALL_CENTER.equals(app.getServiceType())){
            app.setCallCenterNum(getCallCenterAppNum());
        }

        String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
        //TODO 应用新建 时落到测试区域，并指定一个sip接入点
        Area area = new Area();
        area.setId(areaId);
        app.setArea(area);
        AreaSip areaSip = areaSipService.getOneAreaSipByAreaId(areaId);
        app.setAreaSip(areaSip);
        app = this.save(app);
        return app;
    }

    private Long getCallCenterAppNum(){
        //5位编号
        long incr5 = redisCacheService.incr(APP_CC_NUM_PREFIX5);
        //初始始值是10001,因为redis的incr是从1开始的，所以都加上10000
        long num5 = incr5 + 10000;
        //5位编号到59999为止
        if(num5 <= 59999){
            return num5;
        }
        //6位编号
        long incr6 = redisCacheService.incr(APP_CC_NUM_PREFIX6);
        //初始始值是600001,因为redis的incr是从1开始的，所以都加上600000
        long num6 = incr6 + 600000;
        //6位编号到699999为止
        if(num6 <= 699999){
            return num6;
        }
        //7位编号
        long incr7 = redisCacheService.incr(APP_CC_NUM_PREFIX7);
        //初始始值是7000001,因为redis的incr是从1开始的，所以都加上7000000
        long num7 = incr7 + 7000000;
        //7位编号到7999999为止
        if(num7 <= 7999999){
            return num7;
        }
        //TODO 8位9位
        throw new RuntimeException("编号已满，请联系管理员");
    }

    @Override
    public String findAppSipRegistrar(String appId) {
        //TODO 分机注册信息
        return "待实现";
    }

    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #tenantId + #appId + #service.code")
    public boolean enabledService(String tenantId, String appId, ServiceType service) {
        if(tenantId == null){
            return false;
        }
        if(tenantId == null){
            return false;
        }
        if(service == null){
            return false;
        }
        String field = service.getCode();
        try {
            if(service != ServiceType.CallCenter){//租户功能开关 暂时没有呼叫中心功能
                TenantServiceSwitch serviceSwitch = tenantServiceSwitchService.findOneByTenant(tenantId);
                if(serviceSwitch != null){
                    Integer enabled =  (Integer) BeanUtils.getProperty2(serviceSwitch,field);
                    if(enabled == null || enabled != 1){
                        return false;
                    }
                }
            }
            App app = this.findById(appId);
            if(app == null){
                return false;
            }
            Integer enabled =  (Integer) BeanUtils.getProperty2(app,field);
            if(enabled == null || enabled != 1){
                return false;
            }
        } catch (Throwable e) {
            return false;
        }
        return true;
    }
}
