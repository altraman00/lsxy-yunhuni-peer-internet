package com.lsxy.yunhuni.app.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.api.consume.service.ConsumeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.app.model.AppOnlineAction;
import com.lsxy.yunhuni.api.app.service.AppOnlineActionService;
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.api.exceptions.NotEnoughMoneyException;
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.app.dao.AppOnlineActionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/7/15.
 */
@Service
public class AppOnlineActionServiceImpl extends AbstractService<AppOnlineAction> implements AppOnlineActionService {
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

    @Override
    public BaseDaoInterface<AppOnlineAction, Serializable> getDao() {
        return this.appOnlineActionDao;
    }

    @Override
    public AppOnlineAction findActiveActionByAppId(String appId) {
        AppOnlineAction action = appOnlineActionDao.findByAppIdAndStatus(appId, AppOnlineAction.STATUS_AVTIVE);
        return action;
    }

    @Override
    public void actionOfSelectNum(String appId) {
        App app = appService.findById(appId);
        AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
        //应用上线--选号
        if( app.getStatus() == App.STATUS_OFFLINE &&(activeAction == null || activeAction.getAction() == AppOnlineAction.ACTION_OFFLINE)){
            if(activeAction != null){
                activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                this.save(activeAction);
            }
            AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
            this.save(newAction);
        }else if(activeAction.getAction() != AppOnlineAction.ACTION_SELECT_NUM){
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public AppOnlineAction actionOfInPay(String appId, String ivr, boolean contains) {
        App app = new App();
        app.setId(appId);
        AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
        //应用上线--正在支付
        if(activeAction != null ){
            if(activeAction.getAction() == AppOnlineAction.ACTION_SELECT_NUM && contains){
                //当上一步是应用选号中时，如果ivr属于号码池，则生成新的动作--正在支付
                activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                this.save(activeAction);
                BigDecimal amount = new BigDecimal(1000 + 100);
                AppOnlineAction newAction = new AppOnlineAction(ivr,AppOnlineAction.PAY_STATUS_NOPAID,amount,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_PAYING,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
                return newAction;
            }else if(activeAction.getAction() == AppOnlineAction.ACTION_PAYING){
                //当应用正处于正在支付时，反回当前动作
                return activeAction;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public AppOnlineAction actionOfOnline(String userName, String appId) throws NotEnoughMoneyException{
        App app = appService.findById(appId);
        AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
        //应用上线--支付完成
        if(activeAction != null ){
            if(activeAction.getAction() == AppOnlineAction.ACTION_PAYING){
                //当上一步是应用正在支付中时，如果余额足够，则生成新的动作--上线
                Tenant tenant = tenantService.findTenantByUserName(userName);
                Billing billing = billingService.findBillingByTenantId(tenant.getId());
                if(billing.getBalance().compareTo(activeAction.getAmount()) >= 0){
                    //当应用有ivr功能时，绑定IVR号码绑定
                    //判断ivr号码是否被占用
                    if(app.getIsIvrService() != null && app.getIsIvrService() == 1){
                        ResourceTelenum resourceTelenum = resourceTelenumService.findByTelNumber(activeAction.getTelNumber());
                        if(resourceTelenum != null && resourceTelenum.getStatus() != null && resourceTelenum.getStatus()== ResourceTelenum.STATUS_RENTED){
                            //如果ivr号码被占用，则抛也异常
                            throw new TeleNumberBeOccupiedException("IVR号码已被占用");
                        }else{
                            //保存号码资源
                            resourceTelenum.setTenant(tenant);
                            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
                            resourceTelenumService.save(resourceTelenum);
                            // 保存号码租用关系
                            Date date = new Date();
                            String nextMonth = DateUtils.getNextMonth(DateUtils.getDate(date, "yyyy-MM"), "yyyy-MM");
                            Date expireDate = DateUtils.parseDate(nextMonth, "yyyy-MM");    //号码到期时间
                            ResourcesRent resourcesRent = new ResourcesRent(tenant,app,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),expireDate,ResourcesRent.RENT_STATUS_USING);
                            resourcesRentService.save(resourcesRent);
                        }
                    }
                    //TODO 调用扣费接口
                    billing.setBalance(billing.getBalance().subtract(activeAction.getAmount()));
                    billingService.save(billing);
                    //插入消费记录
                    Consume consume = new Consume(new Date(),"应用上线",activeAction.getAmount(),"应用上线",appId,tenant);
                    consumeService.save(consume);
                    //将上一步设为已支付和完成
                    activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                    activeAction.setPayStatus(AppOnlineAction.PAY_STATUS_PAID);
                    this.save(activeAction);
                    //先成新的动作--生成新的动作--上线中
                    AppOnlineAction newAction = new AppOnlineAction(null,null,null,
                            app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_ONLINE,AppOnlineAction.STATUS_AVTIVE);
                    this.save(newAction);
                    //应用状态改为上线
                    app.setStatus(App.STATUS_ONLINE);
                    appService.save(app);

                    return newAction;
                }else{
                    throw new NotEnoughMoneyException("余额不足");
                }
            }else if(activeAction.getAction() == AppOnlineAction.ACTION_ONLINE){
                //当应用正处于已经上线状态时，反回当前动作
                return activeAction;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public AppOnlineAction actionOfDirectOnline(String userName, String appId) {
        App app = appService.findById(appId);
        if(app.getIsIvrService() == null || app.getIsIvrService() == 0){
            AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
            //应用上线--直接上线
            if( app.getStatus() == App.STATUS_OFFLINE &&(activeAction == null || activeAction.getAction() == AppOnlineAction.ACTION_OFFLINE)){
                if(activeAction != null){
                    activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                    this.save(activeAction);
                }
                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_ONLINE,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
                //应用状态改为上线
                app.setStatus(App.STATUS_ONLINE);
                appService.save(app);
                return newAction;
            }else if(activeAction.getAction() == AppOnlineAction.ACTION_ONLINE){
                //当应用正处于已经上线状态时，反回当前动作
                return activeAction;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public AppOnlineAction resetIvr(String userName, String appId) {
        App app = appService.findById(appId);
        AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
        //应用上线--支付完成
        if(activeAction != null ) {
            if (activeAction.getAction() == AppOnlineAction.ACTION_PAYING) {
                //将上一步设为已完成
                activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                this.save(activeAction);
                //插入一条取消支付的动作（状态为已完成）
                AppOnlineAction resetAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_CANCEL_PAY,AppOnlineAction.STATUS_DONE);
                this.save(resetAction);
                //插入一条正在选号的动作
                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_ONLINE,AppOnlineAction.ACTION_SELECT_NUM,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
                return newAction;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

    @Override
    public App offline(String appId) {
        App app = appService.findById(appId);
        AppOnlineAction activeAction = this.findActiveActionByAppId(appId);
        if(activeAction != null && app!= null){
            if(app.getStatus() == App.STATUS_OFFLINE && activeAction.getAction() == AppOnlineAction.ACTION_ONLINE){
                //将上一步设为已完成
                activeAction.setStatus(AppOnlineAction.STATUS_DONE);
                this.save(activeAction);
                //生成新的动作
                AppOnlineAction newAction = new AppOnlineAction(null,null,null,app,AppOnlineAction.TYPE_OFFLINE,AppOnlineAction.ACTION_OFFLINE,AppOnlineAction.STATUS_AVTIVE);
                this.save(newAction);
                //应用状态改为下线
                app.setStatus(App.STATUS_OFFLINE);
                appService.save(app);
                if(app.getIsIvrService() != null && app.getIsIvrService() ==1){
                    //当应用有ivr功能时，改变IVR号码的租用关系
                    List<ResourcesRent> list = resourcesRentService.findByAppId(app.getId());
                    if(list != null && list.size() > 0){
                        for(ResourcesRent rent:list){
                            rent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
                            resourcesRentService.save(rent);
                        }
                    }
                }
                return app;
            }else{
                throw new RuntimeException("数据错误");
            }
        }else{
            throw new RuntimeException("数据错误");
        }
    }

}
