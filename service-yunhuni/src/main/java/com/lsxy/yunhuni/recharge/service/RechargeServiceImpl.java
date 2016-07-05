package com.lsxy.yunhuni.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yuhuni.api.billing.model.Billing;
import com.lsxy.yuhuni.api.billing.service.BillingService;
import com.lsxy.yuhuni.api.recharge.enums.RechargeStatus;
import com.lsxy.yuhuni.api.recharge.enums.RechargeType;
import com.lsxy.yuhuni.api.recharge.model.Recharge;
import com.lsxy.yuhuni.api.recharge.service.RechargeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.yunhuni.recharge.dao.RechargeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;

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
    public Recharge createRecharge(String username, String type, Double amount) throws Exception {
        Recharge recharge = null;
        //充值类型一定要是规定好的类型,当没有该类型时，枚举类会抛出IllegalArgumentException异常
        RechargeType rechargeType = RechargeType.valueOf(type);
        if(rechargeType != null && amount > 0 && username != null){
            Tenant tenant = tenantService.findTenantByUserName(username);
            if(tenant != null){
                BigDecimal bg = new BigDecimal(amount);
                amount = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
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
    public Recharge paySuccess(String orderId) throws MatchMutiEntitiesException {
        Recharge recharge = rechargeDao.findByOrderId(orderId);
        if(recharge != null){
            String status = recharge.getStatus();
            //如果充值记录是未支付状态，则将支付状态改成已支付，并将钱加到账务表里
            if(RechargeStatus.NOTPAID.name().equals(status)){
                //状态变成已支付
                recharge.setStatus(RechargeStatus.PAID.name());
                Tenant tenant = recharge.getTenant();
                //更新账务表的余额
                Billing billing = billingService.findBillingByTenantId(tenant.getId());
                billing.setBalance(billing.getBalance() + recharge.getAmount());
                rechargeDao.save(recharge);
                billingService.save(billing);
            }
        }
        return  recharge;
    }
}
