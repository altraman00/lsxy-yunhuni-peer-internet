package com.lsxy.yunhuni.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yunhuni.api.recharge.enums.RechargeType;
import com.lsxy.yunhuni.api.recharge.model.Recharge;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.recharge.dao.RechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by liups on 2016/7/1.
 */
@Service
public class RechargeServiceImpl extends AbstractService<Recharge> implements RechargeService {

    @Autowired
    RechargeDao rechargeDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    BillingService billingService;

    @Autowired
    EntityManager em;

    @Autowired
    CalBillingService calBillingService;

    @Override
    public BaseDaoInterface<Recharge, Serializable> getDao() {
        return rechargeDao;
    }

    @Override
    public Recharge createRecharge(String username, String type, BigDecimal amount){
        Recharge recharge = null;
        //充值类型一定要是规定好的类型,当没有该类型时，枚举类会抛出IllegalArgumentException异常
        RechargeType rechargeType = RechargeType.valueOf(type);
        if(rechargeType != null && amount.compareTo(new BigDecimal(0))==1 && username != null){
            Tenant tenant = tenantService.findTenantByUserName(username);
            if(tenant != null){
                String orderId = UUIDGenerator.uuid();
                recharge = new Recharge(tenant,amount,rechargeType, RechargeStatus.NOTPAID,orderId,null);
                rechargeDao.save(recharge);
            }
        }
        return recharge;
    }

    @Override
    public Recharge getRechargeByOrderId(String orderId) {
        return rechargeDao.findByOrderId(orderId);
    }

    @Override
    public Recharge paySuccess(String orderId, BigDecimal totalFee) throws MatchMutiEntitiesException {
        Recharge recharge = rechargeDao.findByOrderId(orderId);
        BigDecimal amount = recharge.getAmount().setScale(2, BigDecimal.ROUND_HALF_UP);

        if(recharge != null && amount.compareTo(totalFee)==0){
            String status = recharge.getStatus();
            //如果充值记录是未支付状态，则将支付状态改成已支付，并将钱加到账务表里
            if(RechargeStatus.NOTPAID.name().equals(status)){
                Date curTime = new Date();
                //状态变成已支付
                recharge.setStatus(RechargeStatus.PAID.name());
                recharge.setPayTime(curTime);
                rechargeDao.save(recharge);
                Tenant tenant = recharge.getTenant();
                //redis插入今日充值
                calBillingService.incRecharge(tenant.getId(),curTime,recharge.getAmount());
            }
        }
        return  recharge;
    }


    @Override
    public Page<Recharge> pageListByUserNameAndTime(String userName, Integer pageNo, Integer pageSize, Date startTime, Date endTime) throws MatchMutiEntitiesException {
        Page<Recharge> page = null;
        Tenant tenant = tenantService.findTenantByUserName(userName);
        if(tenant != null){
            if(startTime != null && endTime != null){
                String hql = "from Recharge obj where obj.tenant.id=?1 and obj.createTime between ?2 and ?3 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,tenant.getId(),startTime,endTime);
            }else if(startTime != null && endTime == null){
                String hql = "from Recharge obj where obj.tenant.id=?1 and obj.createTime >= ?2 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,tenant.getId(),startTime);
            }else if(startTime == null && endTime != null){
                String hql = "from Recharge obj where obj.tenant.id=?1 and obj.createTime <= ?2 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,tenant.getId(),endTime);
            }else{
                String hql = "from Recharge obj where obj.tenant.id=?1 order by obj.createTime desc";
                page =  this.pageList(hql,pageNo,pageSize,tenant.getId());
            }
        }
        return page;
    }

    @Override
    public boolean doRecharge(String tenantId, BigDecimal amount) {
        if(StringUtil.isEmpty(tenantId)){
            throw new IllegalArgumentException();
        }
        if(amount == null || amount.doubleValue() < 0){
            throw new IllegalArgumentException();
        }
        Tenant tenant = tenantService.findById(tenantId);
        if(tenant == null){
            throw new IllegalArgumentException();
        }
        String orderId = UUIDGenerator.uuid();
        Date curTime = new Date();
        Recharge recharge = new Recharge(tenant,amount,RechargeType.RENGONG, RechargeStatus.PAID,orderId,curTime);
        rechargeDao.save(recharge);
        // redis插入今日充值
        calBillingService.incRecharge(tenantId,curTime,amount);
        return true;
    }

    @Override
    public List<Recharge> listByTenant(String tenant) {
        String hql = "from Recharge obj where obj.tenant.id=?1 order by obj.createTime desc";
        return this.list(hql,tenant);
    }

    @Override
    public Page<Recharge> pageListByTenant(String tenant, Integer pageNo, Integer pageSize) {
        Page<Recharge> page = null;
        String hql = "from Recharge obj where obj.tenant.id=?1 and obj.status ='PAID' order by obj.createTime desc";
        page =  this.pageList(hql,pageNo,pageSize,tenant);
        return page;
    }

}
