package com.lsxy.yunhuni.resourceTelenum.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourcesRentDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 租户号码租用service
 * Created by zhangxb on 2016/7/1.
 */
@Service
public class ResourcesRentServiceImpl extends AbstractService<ResourcesRent> implements ResourcesRentService {
    Logger logger = LoggerFactory.getLogger(ResourcesRentServiceImpl.class);
    @Autowired
    public ResourcesRentDao resourcesRentDao;
    @Override
    public BaseDaoInterface<ResourcesRent, Serializable> getDao() {
        return this.resourcesRentDao;
    }
    @Autowired
    public TenantService tenantService;
    @Autowired
    ResourceTelenumService resourceTelenumService;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    ConsumeService consumeService;

    @Override
    public Page<ResourcesRent> pageListByTenantId(String userName,int pageNo, int pageSize)   {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String hql = "from ResourcesRent obj where obj.tenant.id=?1 and obj.rentStatus<>3 ";
        Page<ResourcesRent> page =  this.pageList(hql,pageNo,pageSize,tenant.getId());
        return page;
    }

    @Override
    public List<ResourcesRent> findByAppId(String appId) {
        return resourcesRentDao.findByAppIdAndRentStatus(appId,ResourcesRent.RENT_STATUS_USING);
    }

    @Override
    public ResourcesRent findByResourceTelenumIdAndStatus(String id, int status) {
        return resourcesRentDao.findByResourceTelenumIdAndRentStatus(id,status);
    }

    @Override
    public String[] findOwnUnusedNum(Tenant tenant) {
        List<String> telNums = new ArrayList<>();
        List<ResourcesRent> list = resourcesRentDao.findByTenantIdAndRentStatus(tenant.getId(),ResourcesRent.RENT_STATUS_UNUSED);
        if(list != null && list.size()>0){
            for(ResourcesRent rent:list){
                String telNumber = rent.getResourceTelenum().getTelNumber();
                telNums.add(telNumber);
            }
        }
        return telNums.toArray(new String[]{});
    }

    @Override
    public void cleanExpireTelnumResourceRent() {
        int expire = Integer.parseInt(SystemConfig.getProperty("account.ivr.expire", "7"));
        Date limitTime = new Date(System.currentTimeMillis() - expire * 24 * 60 * 60 * 1000);
        resourceTelenumService.cleanExpireResourceTelnum(limitTime);
        resourcesRentDao.cleanExpireTelnumResourceRent(limitTime);
    }

    @Override
    public void resourcesRentTask(){
        Date curTime = new Date();
        List<ResourcesRent> resourcesRents = resourcesRentDao.findByRentStatusNotAndResTypeAndRentExpireLessThan(ResourcesRent.RENT_STATUS_RELEASE,ResourcesRent.RESTYPE_TELENUM,curTime);
        for(ResourcesRent resourcesRent:resourcesRents){
            String tenantId = null;
            Tenant tenant = resourcesRent.getTenant();
            if(tenant != null){
                tenantId = tenant.getId();
            }
            String appId = null;
            App app = resourcesRent.getApp();
            if(app != null){
                appId = app.getId();
            }
            if(StringUtils.isNotBlank(tenantId)){
                BigDecimal balance = calBillingService.getBalance(tenantId);
                //TODO 获取每月号码扣费金额
                BigDecimal cost = new BigDecimal(100);
                if(balance.compareTo(cost) == 1 || balance.compareTo(cost) == 0){
                    Date expireDate = DateUtils.getLastTimeOfMonth(curTime);
                    if(logger.isDebugEnabled()){
                        logger.debug("号码租用过期时间：{}",DateUtils.formatDate(expireDate,"yyyy-MM-dd HH:mm:ss"));
                    }
                    resourcesRentDao.updateResourceRentExpireTime(resourcesRent.getId(),expireDate);
                    //TODO 支付
                    //插入消费记录
                    Consume consume = new Consume(curTime,Consume.RENT_NUMBER,cost,"号码租用",appId,tenant);
                    consumeService.save(consume);
                    //Redis中消费增加
                    calBillingService.incConsume(tenant.getId(),curTime,cost);
                }
            }
        }
    }
}
