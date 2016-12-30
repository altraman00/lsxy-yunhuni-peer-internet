package com.lsxy.yunhuni.resourceTelenum.service;


import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.billing.model.Billing;
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
import com.lsxy.yunhuni.api.config.model.GlobalConfig;
import com.lsxy.yunhuni.api.config.model.TenantConfig;
import com.lsxy.yunhuni.api.config.service.GlobalConfigService;
import com.lsxy.yunhuni.api.config.service.TenantConfigService;
import com.lsxy.yunhuni.api.consume.enums.ConsumeCode;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.file.service.VoiceFileRecordService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    public void monthlyRentTask() {
        resourcesRentTask();
        recordingVoiceFileTask();
        agentMonthTask();
    }
    private void agentMonthTask(){
        Date curTime = new Date();
        Calendar cal = Calendar.getInstance();
//        cal.add(Calendar.DAY_OF_MONTH,-timeLong);
        cal.set(Calendar.HOUR_OF_DAY, 0);//时
        cal.set(Calendar.MINUTE, 0);//分
        cal.set(Calendar.SECOND, 0);//秒
        cal.set(Calendar.MILLISECOND,0);//毫秒
        //获取删除时间
        Date endTime =  cal.getTime();
        cal.add(Calendar.MONTH, -1);// 月份减1
        Date startTime = cal.getTime();
        List<Tenant> tenants = tenantService.getListByPage();
        for(int i=0;i<tenants.size();i++){
            List<App> apps = appService.getAppsByTenantId(tenants.get(i).getId());
            for(int j=0;j<apps.size();j++){
                long sum =  sumAgentNum(tenants.get(i).getId(),apps.get(j).getId(),startTime,endTime);
                BigDecimal cost = calCostService.calCost(ProductCode.call_center_month.name(),tenants.get(i).getId());
                Consume consume = new Consume(new Date(), ConsumeCode.call_center_month.name(),cost.multiply(new BigDecimal(sum)),"应用id["+apps.get(j).getId()+"]总共有"+sum+"个坐席",apps.get(j).getId(),tenants.get(i).getId(),null);
                consumeService.consume(consume);
            }
        }

    }
    public long sumAgentNum(String tenantId, String appId, Date startTime, Date endTime) {
        String sql = " select count(1) from (select DISTINCT channel,name  from db_lsxy_bi_yunhuni.tb_bi_call_center_agent_action_log where tenant_id=? and app_id and  action=1 and create_time BETWEEN ? and ? ) a" ;
        return jdbcTemplate.queryForObject(sql,Long.class,tenantId,appId,startTime,endTime);
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
                BigDecimal cost = calCostService.calCost(ProductCode.rent_number_month.name(),tenantId);
                if(balance.compareTo(cost) == 1 || balance.compareTo(cost) == 0){
                    Date expireDate = DateUtils.getLastTimeOfMonth(curTime);
                    if(logger.isDebugEnabled()){
                        logger.debug("号码租用过期时间：{}",DateUtils.formatDate(expireDate,"yyyy-MM-dd HH:mm:ss"));
                    }
                    resourcesRentDao.updateResourceRentExpireTime(resourcesRent.getId(),expireDate);
                    //TODO 支付
                    //插入消费记录
                    Consume consume = new Consume(curTime, ConsumeCode.rent_number_month.name(),cost,ConsumeCode.rent_number_month.getName(),appId,tenant.getId(),null);
                    consumeService.consume(consume);
                }
            }
        }
    }

    @Override
    public void recordingVoiceFileTask() {
        int globalRecording = 7;
        Pattern pattern = Pattern.compile("^[0-9]*[1-9][0-9]*$");
        GlobalConfig globalConfig = globalConfigService.findByTypeAndName(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING);
        if(globalConfig!=null&&StringUtils.isNotEmpty(globalConfig.getValue())) {
            //全局录音文件存储时长时间
            Matcher matcher = pattern.matcher(globalConfig.getValue());
            if (matcher.matches()) {
                globalRecording = Integer.valueOf(globalConfig.getValue());
            }
        }
        List<TenantConfig> list = tenantConfigService.getPageByTypeAndKeyName(GlobalConfig.TYPE_RECORDING,GlobalConfig.KEY_RECORDING);
        for(int i=0;i<list.size();i++){
            TenantConfig tenantConfig = list.get(i);
            if(StringUtils.isNotEmpty(tenantConfig.getValue())&&StringUtils.isNotEmpty(tenantConfig.getTenantId())&&StringUtils.isNotEmpty(tenantConfig.getAppId())){
                int tenantRecording = 0;
                Matcher matcher1 = pattern.matcher(tenantConfig.getValue());
                if(matcher1.matches()){
                    tenantRecording = Integer.valueOf(tenantConfig.getValue());
                }
                //全局配置的录音文件存储时间不收费，租户的应用下的配置如果大于全局配置的话需要收费
                if(tenantRecording>globalRecording){
                    recordCost(tenantConfig.getTenantId(),tenantConfig.getAppId());
                }
            }
        }

    }
    @Override
    public boolean recordCost(String tenantId,String appId){
        boolean flag = true;
        Tenant tenant = tenantService.findById(tenantId);
        App app = appService.findById(appId);
        BigDecimal cost = calCostService.calCost(ProductCode.recording_memory.name(),tenant.getId());
        if(tenant!=null&&app!=null){
            //获取租户应用下的录音文件 isDeleted
            long size = voiceFileRecordService.getSumSize(tenant.getId(),app.getId());
            long g = 1024*1024*1024;
            if(size>0) {
                long s1 = (size / g) + (g % g > 0 ? 1 : 0);
                BigDecimal temp = cost.multiply(new BigDecimal(s1));
                Billing billing = calBillingService.getCalBilling(tenant.getId());
                if(billing.getBalance().compareTo(temp)==-1) {
                    flag = false;
                }
                Consume consume = new Consume(new Date(), ConsumeCode.recording_memory.name(),temp,ConsumeCode.recording_memory.getName(),app.getId(),tenant.getId(),null);
                consumeService.consume(consume);
            }
        }
        return flag;
    }

    @Override
    @CacheEvict(value = "entity", key = "'entity_' + #id", beforeInvocation = true)
    public void release(String id) {
        ResourcesRent resourcesRent = this.findById(id);
        resourcesRent.setRentStatus(ResourcesRent.RENT_STATUS_RELEASE);
        this.save(resourcesRent);
        ResourceTelenum resourceTelenum =  resourcesRent.getResourceTelenum();
        resourceTelenum.setTenantId(null);
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
}
