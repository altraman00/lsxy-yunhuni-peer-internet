package com.lsxy.framework.api.recharge.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.recharge.model.Recharge;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;

/**
 * 充值相关接口
 * Created by liups on 2016/7/1.
 */
public interface RechargeService extends BaseService<Recharge> {
    /**
     * 生成充值订单
     * @param userName 用户账号名
     * @param type  充值类型
     * @param amount 充值金额
     * @return
     */
    Recharge createRecharge(String userName, String type, Double amount) throws Exception;

    /**
     * 根据orderId获取充值订单
     * @param orderId
     * @return
     */
    Recharge getRechargeByOrderId(String orderId);
}
