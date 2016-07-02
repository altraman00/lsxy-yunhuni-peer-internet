package com.lsxy.framework.recharge.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.recharge.enums.RechargeStatus;
import com.lsxy.framework.api.recharge.enums.RechargeType;
import com.lsxy.framework.api.recharge.model.Recharge;
import com.lsxy.framework.api.recharge.service.RechargeService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.recharge.dao.RechargeDao;
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
}
