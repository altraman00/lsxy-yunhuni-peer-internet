package com.lsxy.yunhuni.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yunhuni.api.recharge.enums.RechargeType;
import com.lsxy.yunhuni.api.recharge.model.Recharge;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.recharge.dao.RechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

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

    @Override
    public BaseDaoInterface<Recharge, Serializable> getDao() {
        return rechargeDao;
    }

    @Override
    public Recharge createRecharge(String username, String type, BigDecimal amount) throws Exception {
        Recharge recharge = null;
        //充值类型一定要是规定好的类型,当没有该类型时，枚举类会抛出IllegalArgumentException异常
        RechargeType rechargeType = RechargeType.valueOf(type);
        if(rechargeType != null && amount.compareTo(new BigDecimal(0))==1 && username != null){
            Tenant tenant = tenantService.findTenantByUserName(username);
            if(tenant != null){
                String orderId = UUIDGenerator.uuid();
                recharge = new Recharge(tenant,amount,rechargeType, RechargeStatus.NOTPAID,orderId);
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
        if(recharge != null && recharge.getAmount().equals(totalFee)){
            String status = recharge.getStatus();
            //如果充值记录是未支付状态，则将支付状态改成已支付，并将钱加到账务表里
            if(RechargeStatus.NOTPAID.name().equals(status)){
                //状态变成已支付
                recharge.setStatus(RechargeStatus.PAID.name());
                Tenant tenant = recharge.getTenant();
                //更新账务表的余额
                Billing billing = billingService.findBillingByTenantId(tenant.getId());
                billing.setBalance(billing.getBalance().add(recharge.getAmount()));
                rechargeDao.save(recharge);
                billingService.save(billing);
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
}
