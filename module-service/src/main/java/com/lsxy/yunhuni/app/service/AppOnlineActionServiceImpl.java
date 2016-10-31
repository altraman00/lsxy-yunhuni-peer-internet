package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.service.AreaService;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelnumToLineGatewayService;
import com.lsxy.yunhuni.app.dao.AppOnlineActionDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


/**
 * Created by liups on 2016/7/15.
 */
@Service
public class AppOnlineActionServiceImpl extends AbstractService<AppOnlineAction> implements AppOnlineActionService {
    Logger logger = LoggerFactory.getLogger(AppOnlineActionServiceImpl.class);
    @Autowired
    AppOnlineActionDao appOnlineActionDao;

    @Autowired
    AppService appService;

    @Autowired
    BillingService billingService;

    @Autowired
    ConsumeService consumeService;

    @Autowired
    TenantService tenantService;

    @Autowired
    ResourceTelenumService resourceTelenumService;

    @Autowired
    ResourcesRentService resourcesRentService;

    @Autowired
    TelnumToLineGatewayService telnumToLineGatewayService;

    @Autowired
    AreaService areaService;

    @Autowired
    CalBillingService calBillingService;

    @Override
    public BaseDaoInterface<AppOnlineAction, Serializable> getDao() {
        return this.appOnlineActionDao;
    }

    @Override
    public AppOnlineAction findActiveActionByAppId(String appId) {
        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
        if(actionList != null && actionList.size() > 0){
            return actionList.get(0);
        }else{
            return null;
        }
    }

    @Override
    public void actionOfSelectNum(String appId) {
        App app = appService.findById(appId);
        AppOnlineAction action = null;
        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
        if(actionList != null && actionList.size() > 0){
            action = actionList.get(0);
        }
        //应用上线--选号
        if( app.getStatus() == App.STATUS_OFFLINE ){
            if(action == null){
                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
            }else if(action != null && action.getAction() != AppOnlineAction.ACTION_SELECT_NUM){
                for(AppOnlineAction a:actionList){
                    a.setStatus(AppOnlineAction.STATUS_DONE);
                    this.save(a);
                }
                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
            }
        }else {
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public AppOnlineAction actionOfOnline(Tenant tenant, String appId,String nums){
        List<String> numList = Arrays.asList(nums.split(","));
        App app = appService.findById(appId);
        AppOnlineAction action = null;
        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
        if(actionList != null && actionList.size() > 0){
            action = actionList.get(0);
        }
        //应用上线
        if(action != null ){
            if(action.getAction() == AppOnlineAction.ACTION_SELECT_NUM){
                //当应用有ivr功能时(或者为呼叫中心应用)，绑定IVR号码绑定
                //判断ivr号码是否被占用
                String areaId = this.bindNumToApp(app, numList, tenant);

                //TODO 绑定应用与区域的关系
                Area oldArea = app.getArea();
                if(oldArea != null && !oldArea.getId().equals(areaId)){
                    //TODO 区域不一样时，进行区域迁移操作
//                    throw new RuntimeException("所选号码区域与应用原来区域不一样，请重新选择号码，或联系客服");
                }
                Area area = new Area();
                area.setId(areaId);
                app.setArea(area);

                //将上一步设为完成
                for(AppOnlineAction a:actionList){
                    a.setStatus(AppOnlineAction.STATUS_DONE);
                    this.save(a);
                }
                //先成新的动作--生成新的动作--上线中
                AppOnlineAction newAction = new AppOnlineAction(nums,null,null,
                        app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_ONLINE,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);

                //应用状态改为上线
                app.setStatus(App.STATUS_ONLINE);
                appService.save(app);

                return newAction;
            }else if(action.getAction() == AppOnlineAction.ACTION_ONLINE){
                //当应用正处于已经上线状态时，反回当前动作
                return action;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    /**
     * 绑定号码到应用
     * @param app
     * @param nums
     * @param tenant
     */
    private String bindNumToApp(App app, List<String> nums, Tenant tenant) {
        String areaId = null;
        //
        boolean isCalled = false;
        //TODO
        for(String num : nums){
            ResourceTelenum resourceTelenum = resourceTelenumService.findByTelNumber(num);
            if(resourceTelenum.getStatus()== ResourceTelenum.STATUS_RENTED && tenant.getId().equals(resourceTelenum.getTenant().getId())){
               //是这个租户，则查询租用记录，有没有正在用的
               ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumIdAndStatus(resourceTelenum.getId(),ResourcesRent.RENT_STATUS_UNUSED);
               if(resourcesRent == null){
                   throw new TeleNumberBeOccupiedException("此号码已被应用占用：" + resourceTelenum.getTelNumber());
               }else if(!resourcesRent.getTenant().getId().equals(tenant.getId()) || resourcesRent.getApp() != null){
                   throw new TeleNumberBeOccupiedException("此号码不属于本租户：" + resourceTelenum.getTelNumber());
               }else{
                   if(StringUtils.isBlank(areaId)){
                       //TODO 设置区域
                       areaId = "area001";
                       //TODO 区域不同，抛异常
                   }else if(!areaId.equals("area001")){
                       //TODO 抛异常
                   }
                   //TODO 号码是否是可呼出
                   isCalled = true;

                   resourcesRent.setApp(app);
                   resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_USING);
                   resourcesRentService.save(resourcesRent);
               }
            }else{
                //如果号码被占用，则抛出异常
                throw new TeleNumberBeOccupiedException("有一个或多个号码不属于本租户");
            }
        }
        if((app.getIsIvrService() != null && app.getIsIvrService() == 1)||(app.getIsCallCenter() != null && app.getIsCallCenter() == 1)) {
            if(!isCalled) {
                //TODO 抛异常，没有可呼出号码
            }
        }
        return areaId;
    }


    @Override
    public App offline(String appId) {
        App app = appService.findById(appId);
        if(app!= null && app.getStatus() == App.STATUS_ONLINE){
            List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
            if(actionList != null && actionList.size() > 0){
                //将上一步设为已完成
                for(AppOnlineAction a:actionList){
                    a.setStatus(AppOnlineAction.STATUS_DONE);
                    this.save(a);
                }
            }

            //生成新的动作
            AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_OFFLINE,AppOnlineAction.ACTION_OFFLINE,AppOnlineAction.STATUS_AVTIVE);
            this.save(newAction);
            //应用状态改为下线
            app.setStatus(App.STATUS_OFFLINE);
            //TODO 当区域和测试区域不一样时，如果是呼叫中心，则分机设为不可用
            //TODO 当区域和测试区域不一样时，进行区域迁移
            //TODO 应用区域设置为测试区域
            appService.save(app);
            //改变号码的租用关系
            List<ResourcesRent> rents = resourcesRentService.findByAppId(app.getId());
            if(rents != null && rents.size() >0){
                for(ResourcesRent rent:rents){
                    rent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
                    rent.setApp(null);
                    resourcesRentService.save(rent);
                }
            }
            return app;
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public void resetAppOnlineAction(String appId) {
        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
        if(actionList != null && actionList.size() > 0){
            for(AppOnlineAction action : actionList){
                action.setStatus(AppOnlineAction.STATUS_DONE);
                this.save(action);
            }
        }
    }

    @Override
    public String findLastOnlineNums(String appId) {
        AppOnlineAction action = appOnlineActionDao.findFirstByAppIdAndActionOrderByCreateTimeDesc(appId,AppOnlineAction.ACTION_ONLINE);
        if(action != null){
            return action.getTelNumber();
        }else{
            return null;
        }
    }

}
