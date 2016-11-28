package com.lsxy.yunhuni.resourceTelenum.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.app.model.App;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourceTelenum;
import com.lsxy.yunhuni.api.resourceTelenum.model.ResourcesRent;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrder;
import com.lsxy.yunhuni.api.resourceTelenum.model.TelenumOrderItem;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourceTelenumService;
import com.lsxy.yunhuni.api.resourceTelenum.service.ResourcesRentService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderItemService;
import com.lsxy.yunhuni.api.resourceTelenum.service.TelenumOrderService;
import com.lsxy.yunhuni.resourceTelenum.dao.ResourcesRentDao;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;

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
    @Autowired
    TelenumOrderService telenumOrderService;
    @Autowired
    TelenumOrderItemService telenumOrderItemService;
    @Autowired
    CalCostService calCostService;
    @Override
    public Page<ResourcesRent> pageListByTenantId(String userName,int pageNo, int pageSize)   {
        Tenant tenant = null;
        tenant = tenantService.findById(userName);
        if(tenant==null){
            tenant = tenantService.findTenantByUserName(userName);
        }
        if(tenant==null){
            throw new RuntimeException("租户不存在");
        }
        String hql = "from ResourcesRent obj where obj.tenant.id=?1 and obj.rentStatus<>3 order by obj.createTime desc";
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
    public ResourcesRent findByResourceTelenumId(String id) {
        String hql = "  From ResourcesRent obj WHERE obj.rentStatus<>'"+ResourcesRent.RENT_STATUS_RELEASE+"' AND obj.resourceTelenum.id='"+id+"' ";
        try {
            return this.findUnique(hql);
        } catch (MatchMutiEntitiesException e) {
            return null;
        }
    }

    @Override
    public ResourcesRent findByResDataAndRentStatus(String resData, int status) {
        return resourcesRentDao.findByResDataAndRentStatus(resData,status);
    }

    @Override
    public List<ResourceTelenum> findOwnUnusedNum(Tenant tenant) {
        List<ResourceTelenum> telNums = new ArrayList<>();
        List<ResourcesRent> list = resourcesRentDao.findByTenantIdAndRentStatus(tenant.getId(),ResourcesRent.RENT_STATUS_UNUSED);
        if(list != null && list.size()>0){
            for(ResourcesRent rent:list){
                ResourceTelenum telNumber = rent.getResourceTelenum();
                if(telNumber != null){
                    telNums.add(telNumber);
                }
            }
        }
        return telNums;
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
            String appId = "0";
            App app = resourcesRent.getApp();
            if(app != null){
                appId = app.getId();
            }
            if(StringUtils.isNotBlank(tenantId)){
                BigDecimal balance = calBillingService.getBalance(tenantId);
                //获取每月号码扣费金额
                BigDecimal cost = calCostService.calCost(ProductItem.RENT_NUMBER_MONTH,tenantId);
                if(balance.compareTo(cost) == 1 || balance.compareTo(cost) == 0){
                    Date expireDate = DateUtils.getLastTimeOfMonth(curTime);
                    if(logger.isDebugEnabled()){
                        logger.debug("号码租用过期时间：{}",DateUtils.formatDate(expireDate,"yyyy-MM-dd HH:mm:ss"));
                    }
                    resourcesRentDao.updateResourceRentExpireTime(resourcesRent.getId(),expireDate);
                    //TODO 支付
                    //插入消费记录
                    Consume consume = new Consume(curTime, ConsumeCode.rent_number_month.name(),cost,ConsumeCode.rent_number_month.getName(),appId,tenant);
                    consumeService.save(consume);
                    //Redis中消费增加
                    calBillingService.incConsume(tenant.getId(),curTime,cost);
                }
            }
        }
    }

    @Override
    public List<ResourcesRent> findByTenantId(String tenantId) {
        List<Integer> status = Arrays.asList(1, 2);
        return resourcesRentDao.findByTenantIdAndRentStatusIn(tenantId,status);
    }

    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void release(String id) {
        ResourcesRent resourcesRent = this.findById(id);
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        this.save(resourcesRent);
        ResourceTelenum resourceTelenum =  resourcesRent.getResourceTelenum();
        resourceTelenum.setTenant(null);
        resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
        resourceTelenumService.save(resourceTelenum);
    }

    @Override
    public void telnumPlay(String id,Tenant tenant) {
        TelenumOrder temp = telenumOrderService.findById(id);
        //更新记录
        temp.setStatus(TelenumOrder.Status_success);
        telenumOrderService.save(temp);
        List<TelenumOrderItem> list = telenumOrderItemService.findByTenantIdAndTelenumOrderId(tenant.getId(), temp.getId());
        for (int i = 0; i < list.size(); i++) {
            ResourceTelenum resourceTelenum = list.get(i).getTelnum();
            resourceTelenum.setStatus(ResourceTelenum.STATUS_RENTED);
            resourceTelenum.setTenant(tenant);
            resourceTelenum = resourceTelenumService.save(resourceTelenum);
            ResourcesRent resourcesRent = new ResourcesRent();
            resourcesRent.setTenant(tenant);
            resourcesRent.setResourceTelenum(resourceTelenum);
            resourcesRent.setResData(resourceTelenum.getTelNumber());
            resourcesRent.setResName("号码资源");
            resourcesRent.setResType("1");
            Date date = DateUtils.getLastTimeOfMonth(new Date());
            resourcesRent.setRentExpire(date);
            resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
            this.save(resourcesRent);
        }
        //扣费
        Consume consume = new Consume(new Date(), ConsumeCode.rent_number.name(), temp.getAmount(), ConsumeCode.rent_number.getName(), "0", tenant);
        consumeService.consume(consume);
        //号码月租费
        BigDecimal cost = calCostService.calCost(ProductItem.RENT_NUMBER_MONTH,tenant.getId());
        BigDecimal bigDecimal = cost.multiply(new BigDecimal(list.size()));
        Consume consume1 = new Consume(new Date(), ConsumeCode.rent_number_month.name(), bigDecimal, ConsumeCode.rent_number_month.getName(), "0", tenant);
        consumeService.consume(consume1);
    }


    @Override
    public void telnumDelete(String id,Tenant tenant) {
        TelenumOrder temp = telenumOrderService.findById(id);
        temp.setStatus(TelenumOrder.status_fail);
        telenumOrderService.save(temp);
        List<TelenumOrderItem> list =  telenumOrderItemService.findByTenantIdAndTelenumOrderId(tenant.getId(),temp.getId());
        for(int i=0;i<list.size();i++){
            ResourceTelenum resourceTelenum = list.get(i).getTelnum();
            resourceTelenum.setStatus(ResourceTelenum.STATUS_FREE);
            resourceTelenumService.save(resourceTelenum);
        }
    }
    @Override
    public TelenumOrder telnumNew(Tenant tenant,String[] numIds) {
        TelenumOrder telenumOrder = new TelenumOrder();
        telenumOrder.setTenantId(tenant.getId());
        telenumOrder.setStatus(TelenumOrder.status_await);
        telenumOrder = telenumOrderService.save(telenumOrder);
        List<String> list = new ArrayList();
        BigDecimal bigDecimal = new BigDecimal(0);
        for(int i=0;i<numIds.length;i++){
            ResourceTelenum resourceTelenum = resourceTelenumService.findById(numIds[i]);
            if(resourceTelenum==null||StringUtils.isEmpty(resourceTelenum.getId())){
                telenumOrder.setStatus(TelenumOrder.status_fail);
                throw new RuntimeException("订单中有号码不存在");
            }
            if(resourceTelenum.getStatus()==ResourceTelenum.STATUS_FREE) {
                TelenumOrderItem telenumOrderItem = new TelenumOrderItem();
                telenumOrderItem.setTenantId(tenant.getId());
                telenumOrderItem.setAmount(resourceTelenum.getAmount());
                telenumOrderItem.setTelnumOrderId(telenumOrder.getId());
                telenumOrderItem.setTelnum(resourceTelenum);
                telenumOrderItemService.save(telenumOrderItem);
                resourceTelenum.setStatus(ResourceTelenum.STATUS_LOCK);
                resourceTelenumService.save(resourceTelenum);
                list.add(numIds[i]);
                bigDecimal=bigDecimal.add(resourceTelenum.getAmount());
            }else{
//                telenumOrder.setStatus(TelenumOrder.status_fail);
//                for(int j=0;j<list.size();j++){
//                    ResourceTelenum resourceTelenum1 = resourceTelenumService.findById(list.get(j));
//                    resourceTelenum1.setStatus(ResourceTelenum.STATUS_FREE);
//                    resourceTelenumService.save(resourceTelenum1);
//                }
                throw new RuntimeException("订单中有号码不存在");
            }
        }
        telenumOrder.setAmount(bigDecimal);
        Calendar c = Calendar.getInstance();
        telenumOrder.setCreateTime(c.getTime());
        c.add(Calendar.DAY_OF_MONTH, 1);
        telenumOrder.setDeadline(c.getTime());
        telenumOrder = telenumOrderService.save(telenumOrder);
        return telenumOrder;
    }
}
