package com.lsxy.framework.recharge.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.recharge.model.Recharge;

import java.io.Serializable;

/**
 * 充值查询类
 * Created by liups on 2016/7/1.
 */
public interface RechargeDao extends BaseDaoInterface<Recharge, Serializable> {
    /**
     * 根据orderId获取充值记录
     * @param orderId
     * @return
     */
    Recharge findByOrderId(String orderId);
}
