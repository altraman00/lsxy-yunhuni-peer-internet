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
import com.lsxy.yunhuni.api.app.service.AppService;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.exceptions.TeleNumberBeOccupiedException;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 租户号码租用service
 * Created by zhangxb on 2016/7/1.
 */
@Service
public class ResourcesRentServiceImpl extends AbstractService<ResourcesRent> implements ResourcesRentService {
    private static final Logger logger = LoggerFactory.getLogger(ResourcesRentServiceImpl.class);
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
    @Autowired
    GlobalConfigService globalConfigService;
    @Autowired
    TenantConfigService tenantConfigService;
    @Autowired
    AppService appService;
    @Autowired
    VoiceFileRecordService voiceFileRecordService;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Override
    public Page<ResourcesRent> pageListByTenantId(String tenantId,int pageNo, int pageSize)   {
        String hql = "from ResourcesRent obj inner join fetch obj.resourceTelenum where obj.tenant.id=?1 and obj.rentStatus<>3 order by obj.createTime desc";
        Page<ResourcesRent> page =  this.pageList(hql,pageNo,pageSize,tenantId);
        return page;
    }

    @Override
    public Page<ResourcesRent> findByAppId(String appId,int pageNo, int pageSize) {
        String hql = "from ResourcesRent obj inner join fetch obj.resourceTelenum where obj.app.id=?1 and obj.rentStatus = 1 order by obj.lastTime desc";
        return  pageList(hql,pageNo,pageSize,appId);
    }

    @Override
    public Page<ResourcesRent> findBySubaccount(String appId,String subaccountId,int pageNo, int pageSize) {
        String hql = "from ResourcesRent obj inner join fetch obj.resourceTelenum where obj.app.id=?1 and obj.rentStatus = 1 and obj.resourceTelenum.subaccountId = ?2 order by obj.lastTime desc";
        return  pageList(hql,pageNo,pageSize,appId,subaccountId);
    }

    @Override
    public List<ResourcesRent> findByAppId(String appId) {
        String hql = "from ResourcesRent obj inner join fetch obj.resourceTelenum where obj.app.id=?1 and obj.rentStatus = 1 order by obj.lastTime desc";
        return this.list(hql,appId);
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
    public List<ResourceTelenum> findOwnUnusedNum(Tenant tenant,String lastOnlineAreaId) {
        List<ResourceTelenum> telNums = new ArrayList<>();
        List<ResourcesRent> list = resourcesRentDao.findByTenantIdAndRentStatus(tenant.getId(),ResourcesRent.RENT_STATUS_UNUSED);
        if(list != null && list.size()>0){
            for(ResourcesRent rent:list){
                ResourceTelenum telNumber = rent.getResourceTelenum();
                if(telNumber != null){
                    if(StringUtils.isNotBlank(lastOnlineAreaId)){
                        if(lastOnlineAreaId.equals(telNumber.getAreaId())){
                            telNums.add(telNumber);
                        }
                    }else{
                        telNums.add(telNumber);
                    }
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
        List<Object[]> resourcesRents = resourcesRentDao.findInfoExpireRent(curTime);
        for(Object[] rent:resourcesRents){
            String rentId = (String) rent[0];
            String tenantId = (String) rent[1];
            String appId = "0";
            if(rent[2] != null){
                appId = (String) rent[2];
            }
            if(StringUtils.isNotBlank(tenantId)){
                //获取每月号码扣费金额
                BigDecimal cost = calCostService.calCost(ProductCode.rent_number_month.name(),tenantId);
                monthlyRentPay(tenantId, curTime, cost, rentId, appId);
            }
        }
    }

    @Override
    public void payResourcesRent(String tenantId) {
        if(StringUtils.isNotBlank(tenantId)){
            Date curTime = new Date();
            List<Object[]> resourcesRents = resourcesRentDao.findInfoExpireRentByTenantId(tenantId,curTime);
            BigDecimal cost = calCostService.calCost(ProductCode.rent_number_month.name(),tenantId);
            for(Object[] rent:resourcesRents){
                String rentId = (String) rent[0];
                String appId = "0";
                if(rent[2] != null){
                    appId = (String) rent[2];
                }
                //如果有一次余额不够了，说明这个租户没钱了，所以断开循环
                if (!monthlyRentPay(tenantId, curTime, cost, rentId, appId)) break;
            }
        }
    }

    private boolean monthlyRentPay(String tenantId, Date curTime, BigDecimal cost, String rentId, String appId) {
        BigDecimal balance = calBillingService.getBalance(tenantId);
        //获取每月号码扣费金额
        if(balance.compareTo(cost) == 1 || balance.compareTo(cost) == 0){
            Date expireDate = DateUtils.getLastTimeOfMonth(curTime);
            if(logger.isDebugEnabled()){
                logger.debug("更新租用记录：{}，号码租用过期时间：{}",rentId,DateUtils.formatDate(expireDate,"yyyy-MM-dd HH:mm:ss"));
            }
            resourcesRentDao.updateResourceRentExpireTime(rentId,expireDate);
            //TODO 支付
            //插入消费记录
            Consume consume = new Consume(curTime, ConsumeCode.rent_number_month.name(),cost,ConsumeCode.rent_number_month.getName(),appId,tenantId,rentId);
            consumeService.consume(consume);
            return true;
        }else{
            return false;
        }
    }

    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void release(String id) {
        ResourcesRent resourcesRent = this.findById(id);
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        this.save(resourcesRent);
        ResourceTelenum resourceTelenum =  resourcesRent.getResourceTelenum();
        resourceTelenum.setTenantId(null);
        resourceTelenum.setAppId(null);
        resourceTelenum.setSubaccountId(null);
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
            resourceTelenum.setTenantId(tenant.getId());
            resourceTelenum = resourceTelenumService.save(resourceTelenum);
            Date expireDate = DateUtils.getLastTimeOfMonth(new Date());
            ResourcesRent resourcesRent = new ResourcesRent(tenant,null,resourceTelenum,"号码资源",ResourcesRent.RESTYPE_TELENUM,new Date(),expireDate,ResourcesRent.RENT_STATUS_UNUSED);
            this.save(resourcesRent);
        }
        //扣费
        Consume consume = new Consume(new Date(), ConsumeCode.rent_number.name(), temp.getAmount(), ConsumeCode.rent_number.getName(), "0", tenant.getId(),null);
        consumeService.consume(consume);
        //号码月租费
        BigDecimal cost = calCostService.calCost(ProductCode.rent_number_month.name(),tenant.getId());
        BigDecimal bigDecimal = cost.multiply(new BigDecimal(list.size()));
        Consume consume1 = new Consume(new Date(), ConsumeCode.rent_number_month.name(), bigDecimal, ConsumeCode.rent_number_month.getName(), "0", tenant.getId(),null);
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

    @Override
    public void appUnbindAll(String tenantId,String appId) {
        resourcesRentDao.appUnbindAll(tenantId,appId,new Date());
        resourceTelenumService.appUnbindAll(tenantId, appId);
    }

    @Override
    public void unbind(String tenantId, String appId, String rentId) {
        ResourcesRent rent = this.findById(rentId);
        ResourceTelenum resourceTelenum = rent.getResourceTelenum();
        resourceTelenum.setAppId(null);
        resourceTelenum.setSubaccountId(null);
        resourceTelenumService.save(resourceTelenum);
        rent.setApp(null);
        rent.setRentStatus(ResourcesRent.RENT_STATUS_UNUSED);
        this.save(rent);
    }


    @Override
    public String bindNumToAppAndGetAreaId(App app, List<String> nums , boolean isNeedCalled) {
        return bindNumToAppOrSubAccountAndGetAreaId(app,nums,null,isNeedCalled);
    }

    @Override
    public void bindNumToSubaccount(App app, List<String> nums, String subAccountId) {
        bindNumToAppOrSubAccountAndGetAreaId(app,nums,subAccountId,false);
    }

    /**
     * 绑定号码到app或子账号
     * @param app
     * @param nums
     * @param subAccountId
     * @param isNeedCalled
     * @return
     */
    private String bindNumToAppOrSubAccountAndGetAreaId(App app, List<String> nums ,String subAccountId, boolean isNeedCalled) {
        String areaId = app.getOnlineAreaId();
        String tenantId = app.getTenant().getId();
        boolean isCalled = false;
        for(String num : nums){
            if(StringUtils.isNotBlank(num)){
                ResourceTelenum resourceTelenum = resourceTelenumService.findByTelNumber(num);
                if(resourceTelenum != null){
                    if(resourceTelenum.getStatus()== ResourceTelenum.STATUS_RENTED && tenantId.equals(resourceTelenum.getTenantId())){
                        //是这个租户，则查询租用记录，有没有正在用的
                        ResourcesRent resourcesRent = resourcesRentDao.findByResourceTelenumIdAndRentStatus(resourceTelenum.getId(),ResourcesRent.RENT_STATUS_UNUSED);
                        if(resourcesRent == null){
                            throw new RuntimeException("找不到租用记录：" + resourceTelenum.getTelNumber());
                        }else if(!resourcesRent.getTenant().getId().equals(tenantId)){
                            throw new RuntimeException("号码租用记录数据出错：" + resourceTelenum.getTelNumber());
                        }else if(resourcesRent.getApp() != null){
                            //如果subAccountId子账号不为空，则，判断号码是否绑定了子账号
                            if(resourcesRent.getApp().getId().equals(app.getId())){
                                if(StringUtils.isBlank(resourceTelenum.getSubaccountId())){
                                    if(StringUtils.isNotBlank(subAccountId)){
                                        resourceTelenum.setSubaccountId(subAccountId);
                                        resourceTelenumService.save( resourceTelenum);
                                    }
                                }
                            }else{
                                throw new TeleNumberBeOccupiedException("号码已经被应用占用：" + resourceTelenum.getTelNumber());
                            }
                        }else{
                            if(StringUtils.isBlank(areaId)){
                                // 将区域存到一个变量
                                areaId = resourceTelenum.getAreaId();
                            }else if(!areaId.equals(resourceTelenum.getAreaId())){
                                //号码区域不同，抛异常
                                throw new RuntimeException("所选号码不属于同一个区域");
                            }
                            //号码是否是可呼入
                            if("1".equals(resourceTelenum.getIsCalled())){
                                isCalled = true;
                            }
                            resourcesRent.setApp(app);
                            resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_USING);
                            this.save(resourcesRent);
                            //更新号码信息，设置应用
                            resourceTelenum.setAppId(app.getId());
                            resourceTelenum.setSubaccountId(subAccountId);
                            resourceTelenumService.save( resourceTelenum);
                        }
                    }else{
                        //如果号码被占用，则抛出异常
                        throw new TeleNumberBeOccupiedException("有一个或多个号码不属于本租户");
                    }
                }
            }
        }

        if(isNeedCalled) {
            if(!isCalled) {
                //TODO 获取应用所绑定的号码，判断是否有可呼入的
                isCalled =  resourceTelenumService.isCalledByTenantIdAndAppId(app.getTenant().getId(),app.getId());
            }
            if(!isCalled) {
                //TODO 应用原来是否已绑定了呼入号码，没有则抛异常
                //抛异常，没有可呼出号码
                throw new RuntimeException("没有选定可呼入的号码");
            }
        }
        return areaId;
    }

}
