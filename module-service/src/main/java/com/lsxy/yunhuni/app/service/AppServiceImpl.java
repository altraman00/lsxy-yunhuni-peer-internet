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
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.app.dao.AppDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/6/29.
 */
@Service
public class AppServiceImpl extends AbstractService<App> implements AppService {
    private static final String APP_CC_NUM_KEY = "APP_CC_NUM";  //存在redis中的呼叫中心应用自增编号key，以hash来存
    private static final String APP_CC_NUM_FIELD5 = "FIELD5";  //5位编号(算法改进，这个是个一直自增的数字，应用编号见生成方法)

    @Autowired
    private AppDao appDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ResourcesRentService resourcesRentService;
    @Autowired
    private ResourceTelenumService resourceTelenumService;
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
    public List<App> findAppByTenantIdAndServiceType(String tenantId, String serviceType) {
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
    public Page<App> pageList(String[] tenantId, Date date1, Date date2, int state, Integer pageNo, Integer pageSize) {
        String hql = "from App obj where obj.status = ?1 ";
        if(date1!=null&&date2!=null){
            if(tenantId!=null&&tenantId.length>0) {
                String tenantIds = "";
                for(int i=0;i<tenantId.length;i++){
                    tenantIds += " '"+tenantId[i]+"' ";
                    if(i!=(tenantId.length-1)){
                        tenantIds+=",";
                    }
                }
                hql += " and obj.tenant.id in (?2) and obj.lastTime between ?3 and ?4 order by obj.applyTime desc";
                return  this.pageList(hql, pageNo, pageSize, state,tenantIds,date1,date2);
            }else{
                hql += " and obj.lastTime between ?2 and ?3 order by obj.applyTime desc";
                return  this.pageList(hql, pageNo, pageSize, state,date1,date2);
            }
        }else{
            if(tenantId!=null&&tenantId.length>0) {
                String tenantIds = "";
                for(int i=0;i<tenantId.length;i++){
                    tenantIds += " '"+tenantId[i]+"' ";
                    if(i!=(tenantId.length-1)){
                        tenantIds+=",";
                    }
                }
                hql += " and obj.tenant.id in (?2) order by obj.applyTime desc";
                return  this.pageList(hql, pageNo, pageSize, state,tenantIds);
            }else{
                return  this.pageList(hql, pageNo, pageSize, state);
            }
        }
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
            app.setCallCenterNum(getExtensionPrefixNum());
        }

        String areaId = SystemConfig.getProperty("area.server.test.area.id", "area001");
        //TODO 应用新建 时落到测试区域，并指定一个sip接入点
        app.setAreaId(areaId);
        app = this.save(app);
        return app;
    }

    @Override
    public Long getExtensionPrefixNum(){
        //5位编号
        Long incr5 = redisCacheService.getHashOps(APP_CC_NUM_KEY).increment(APP_CC_NUM_FIELD5, 1L);
        //初始始值是10001,因为redis的incr是从1开始的，所以都加上10000
        long num5 = incr5 + 20000;//新的规则是改为2开始，以免和普通手机号有冲突
        //5位编号到59999为止
        if(num5 <= 59999){
            return num5;
        }
        long num6 = num5 - 59999 + 600000;
//        6位编号到699999为止
        if(num6 <= 699999){
            return num6;
        }
        long num7 = num6 - 699999 + 7000000;
        //        7位编号到7999999为止
        if(num7 <= 7999999){
            return num7;
        }

        long num8 = num7 - 7999999 + 80000000;
        //        8位编号到89999999为止
        if(num8 <= 89999999){
            return num8;
        }

        long num9 = num8 - 89999999 + 9000000000L;
        //        9开头到9999999999为止
        if(num9 <= 9999999999L){
            return num9;
        }
        throw new RuntimeException("编号已满，请联系管理员");
    }

    @Override
    @Cacheable(value = "entity", key = "'entity_' + #tenantId + #appId + #service.code")
    public boolean enabledService(String tenantId, String appId, ServiceType service) {
        if(tenantId == null){
            return false;
        }
        if(appId == null){
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

    @Override
    public void deleteApp(String appId) throws InvocationTargetException, IllegalAccessException {
        //TODO 应用删除解除号码绑定
        List<ResourcesRent> rents = resourcesRentService.findByAppId(appId);
        if(rents != null && rents.size() >0){
            for(ResourcesRent rent:rents){
                rent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
                rent.setApp(null);
                resourcesRentService.save(rent);
                //更新号码信息，清除应用
                ResourceTelenum resourceTelenum = rent.getResourceTelenum();
                resourceTelenum.setAppId(null);
                resourceTelenum.setSubaccountId(null);
                resourceTelenumService.save( resourceTelenum);
            }
        }
        this.delete(appId);
    }
}
