package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.model.Area;
import com.lsxy.yunhuni.api.config.service.AreaService;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
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
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
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
            if(action != null){
                for(AppOnlineAction a:actionList){
                    a.setStatus(AppOnlineAction.STATUS_DONE);
                    this.save(a);
                }
            }
            AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
            this.save(newAction);
        }else if(action.getAction() != AppOnlineAction.ACTION_SELECT_NUM){
            throw new RuntimeException("数据错误");
        }
    }

//    @Override
//    public AppOnlineAction actionOfInPay(String appId, String ivr,Tenant tenant, boolean contains) {
//        App app = new App();
//        app.setId(appId);
//        AppOnlineAction action = null;
//        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
//        if(actionList != null && actionList.size() > 0){
//            action = actionList.get(0);
//        }
//        //应用上线--正在支付
//        if(action != null ){
//            if(action.getAction() == AppOnlineAction.ACTION_SELECT_NUM ){
//                //当上一步是应用选号中时，如果ivr属于号码池，则生成新的动作--正在支付
//                if(contains){
//                    for(AppOnlineAction a:actionList){
//                        a.setStatus(AppOnlineAction.STATUS_DONE);
//                        this.save(a);
//                    }
//                    //TODO 根据产品策略获取支付金额
//                    BigDecimal amount = new BigDecimal(1000 + 100);
//                    AppOnlineAction newAction = new AppOnlineAction(ivr, AppOnlineAction.PAY_STATUS_NOPAID,amount,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_PAYING,AppOnlineAction.STATUS_AVTIVE);
//                    this.save(newAction);
//                    return newAction;
//                }else{
//                    ResourceTelenum telenum = resourceTelenumService.findByTelNumber(ivr);
//                    //是这个租户，则查询租用记录，有没有正在用的
//                    if(telenum != null && telenum.getTenant().getId().equals(tenant.getId())){
//                        ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumIdAndStatus(telenum.getId(),ResourcesRent.RENT_STATUS_UNUSED);
//                        if(resourcesRent == null){
//                            throw new TeleNumberBeOccupiedException("IVR号码已被占用");
//                        }else if(!resourcesRent.getTenant().getId().equals(tenant.getId()) || resourcesRent.getApp() != null){
//                            throw new TeleNumberBeOccupiedException("IVR号码已被占用");
//                        }else{
//                            //号码没被占用，创建支付动作（支付金额为0）
//                            for(AppOnlineAction a:actionList){
//                                a.setStatus(AppOnlineAction.STATUS_DONE);
//                                this.save(a);
//                            }
//                            AppOnlineAction newAction = new AppOnlineAction(ivr, AppOnlineAction.PAY_STATUS_NOPAID,new BigDecimal(0),app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_PAYING,AppOnlineAction.STATUS_AVTIVE);
//                            this.save(newAction);
//                            return newAction;
//                        }
//                    }else{
//                        throw new RuntimeException("数据错误");
//                    }
//                }
//            }else if(action.getAction() == AppOnlineAction.ACTION_PAYING){
//                //当应用正处于正在支付时，反回当前动作
//                return action;
//            }else{
//                throw new RuntimeException("数据错误");
//            }
//        }else{
//            throw new RuntimeException("数据错误");
//        }
//    }

//    @Override
//    public AppOnlineAction actionOfOnline(String userName, String appId) throws NotEnoughMoneyException{
//        App app = appService.findById(appId);
//        AppOnlineAction action = null;
//        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
//        if(actionList != null && actionList.size() > 0){
//            action = actionList.get(0);
//        }
//        //应用上线--支付完成
//        if(action != null ){
//            if(action.getAction() == AppOnlineAction.ACTION_PAYING){
//                //当上一步是应用正在支付中时，如果余额足够，则生成新的动作--上线
//                Tenant tenant = tenantService.findTenantByUserName(userName);
//                //调用获取余额接口
//                if(calBillingService.getBalance(tenant.getId()).compareTo(action.getAmount()) >= 0){
//                    //当应用有ivr功能时，绑定IVR号码绑定
//                    //判断ivr号码是否被占用
//                    if(app.getIsIvrService() != null && app.getIsIvrService() == 1){
//                        this.bindNumToApp(app, action.getTelNumber(), tenant);
//                    }
//
//                    //将上一步设为已支付和完成
//                    for(AppOnlineAction a:actionList){
//                        a.setStatus(AppOnlineAction.STATUS_DONE);
//                        if(a.getPayStatus() == AppOnlineAction.PAY_STATUS_NOPAID){
//                            a.setPayStatus(AppOnlineAction.PAY_STATUS_PAID);
//                        }
//                        this.save(a);
//                    }
//                    //先成新的动作--生成新的动作--上线中
//                    AppOnlineAction newAction = new AppOnlineAction(null,null,null,
//                            app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_ONLINE,AppOnlineAction.STATUS_AVTIVE);
//                    this.save(newAction);
//
//                    //TODO 绑定应用与区域的关系
//                    Area oldArea = app.getArea();
//                    String areaId = telnumToLineGatewayService.getAreaIdByTelnum(action.getTelNumber());
//                    if(oldArea != null && !oldArea.getId().equals(areaId)){
//                        throw new RuntimeException("所选号码区域与应用原来区域不一样，请重新选择号码，或联系客服");
//                    }else{
//                        Area area = new Area();
//                        area.setId(areaId);
//                        app.setArea(area);
//                    }
//                    //应用状态改为上线
//                    app.setStatus(App.STATUS_ONLINE);
//                    appService.save(app);
//                    //当支付金额为0时，既上线不用支付，就不用插入消费记录，否则插入消费记录
//                    if(action.getAmount().compareTo(new BigDecimal(0)) == 1){
//                        //支付扣费，并插入消费记录
//                        this.pay(appId, action.getAmount(), tenant);
//                    }
//                    return newAction;
//                }else{
//                    throw new NotEnoughMoneyException("余额不足");
//                }
//            }else if(action.getAction() == AppOnlineAction.ACTION_ONLINE){
//                //当应用正处于已经上线状态时，反回当前动作
//                return action;
//            }else{
//                throw new RuntimeException("数据错误");
//            }
//        }else{
//            throw new RuntimeException("数据错误");
//        }
//    }

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
                //当支付金额为0时，既上线不用支付，就不用插入消费记录，否则插入消费记录
//                if(action.getAmount()!=null && (action.getAmount().compareTo(new BigDecimal(0)) == 1)){
//                    //支付扣费，并插入消费记录
//                    this.pay(appId, action.getAmount(), tenant);
//                }
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

//    /**
//     * 支付扣费，并插入消费记录
//     * @param appId
//     * @param amount
//     * @param tenant
//     */
//    private void pay(String appId, BigDecimal amount, Tenant tenant) {
//        //TODO 支付
//        Date curTime = new Date();
//        //插入消费记录
//        Consume consume = new Consume(curTime, ConsumeCode.rent_number.name(),amount,ConsumeCode.rent_number.getName(),appId,tenant);
//        consumeService.consume(consume);
//    }

//    /**
//     * 如果号码已被租用,则根据租用关系进行判断并处理
//     * @param app 应用
//     * @param tenant 租户
//     * @param resourceTelenum 号码资源
//     */
//    private void alterResourcesRent(App app, Tenant tenant, ResourceTelenum resourceTelenum) {
//        //如果号码被租用
//        //租用是不是这个租户
//        if(!resourceTelenum.getTenant().getId().equals(tenant.getId())){
//            throw new TeleNumberBeOccupiedException("IVR号码已被占用");
//        }else{
//            //是这个租户，则查询租用记录，有没有正在用的
//            ResourcesRent resourcesRent = resourcesRentService.findByResourceTelenumIdAndStatus(resourceTelenum.getId(),ResourcesRent.RENT_STATUS_UNUSED);
//            if(resourcesRent == null){
//                throw new TeleNumberBeOccupiedException("IVR号码已被占用");
//            }else if(!resourcesRent.getTenant().getId().equals(tenant.getId()) || resourcesRent.getApp() != null){
//                throw new TeleNumberBeOccupiedException("IVR号码已被占用");
//            }else{
//                resourcesRent.setApp(app);
//                resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_USING);
//                resourcesRentService.save(resourcesRent);
//            }
//        }
//    }

//    /**
//     * 生成新的号码租用关系
//     * @param app 应用
//     * @param tenant 租户
//     * @param resourceTelenum 号码资源
//     */
//    private void newResourcesRent(App app, Tenant tenant, ResourceTelenum resourceTelenum) {
//        //保存号码资源
//        resourceTelenum.setTenant(tenant);
//        resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
//        resourceTelenumService.save(resourceTelenum);
//        // 保存号码租用关系
//        Date date = new Date();
//
//        Date expireDate = DateUtils.getLastTimeOfMonth(date);
//        if(logger.isDebugEnabled()){
//            logger.debug("号码租用过期时间：{}",DateUtils.formatDate(expireDate,"yyyy-MM-dd HH:mm:ss"));
//        }
//        ResourcesRent resourcesRent = new ResourcesRent(tenant,app,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),expireDate,ResourcesRent.RENT_STATUS_USING);
//        resourcesRentService.save(resourcesRent);
//    }

//    @Override
//    public AppOnlineAction actionOfDirectOnline(String userName, String appId) {
//        App app = appService.findById(appId);
//        if(app.getIsIvrService() == null || app.getIsIvrService() == 0){
//            //应用上线--直接上线
//            AppOnlineAction action = null;
//            List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
//            if(actionList != null && actionList.size() > 0){
//                action = actionList.get(0);
//            }
//            if( app.getStatus() == App.STATUS_OFFLINE ){
//                if(action != null){
//                    for(AppOnlineAction a:actionList){
//                        a.setStatus(AppOnlineAction.STATUS_DONE);
//                        this.save(a);
//                    }
//                }
//                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_ONLINE,AppOnlineAction.STATUS_AVTIVE);
//                this.save(newAction);
//                //应用状态改为上线
//                app.setStatus(App.STATUS_ONLINE);
//                //应用绑定区域
//                if(app.getArea() == null){
//                    app.setArea(areaService.getOneAvailableArea());
//                }
//                appService.save(app);
//                return newAction;
//            }else if(app.getStatus() == App.STATUS_ONLINE ){
//                return action;
//            }else{
//                throw new RuntimeException("数据错误");
//            }
//        }else{
//            throw new RuntimeException("数据错误");
//        }
//    }

//    @Override
//    public AppOnlineAction resetIvr(String userName, String appId) {
//        App app = appService.findById(appId);
//        AppOnlineAction action = null;
//        List<AppOnlineAction> actionList = appOnlineActionDao.findByAppIdAndStatusOrderByCreateTimeDesc(appId, AppOnlineAction.STATUS_AVTIVE);
//        if(actionList != null && actionList.size() > 0){
//            action = actionList.get(0);
//        }
//        //应用上线--支付完成
//        if(action != null ) {
//            if (action.getAction() == AppOnlineAction.ACTION_PAYING) {
//                //将上一步设为已完成
//                for(AppOnlineAction a:actionList){
//                    a.setStatus(AppOnlineAction.STATUS_DONE);
//                    this.save(a);
//                }
//                //插入一条取消支付的动作（状态为已完成）
//                AppOnlineAction resetAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_CANCEL_PAY,AppOnlineAction.STATUS_DONE);
//                this.save(resetAction);
//                //插入一条正在选号的动作
//                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
//                this.save(newAction);
//                return newAction;
//            }else{
//                throw new RuntimeException("数据错误");
//            }
//        }else{
//            throw new RuntimeException("数据错误");
//        }
//    }

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

}
