package com.lsxy.yuhuni.api.recharge.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yuhuni.api.recharge.model.Recharge;

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

    /**
     * 充值成功后的处理
     * 如果没有做过处理，根据订单号在系统中查到该笔订单的详细，并执行业务程序
     * 如果有做过处理，不执行商户的业务程序
     * @param orderId 充值记录的orderId
     * @return
     */
    Recharge paySuccess(String orderId) throws MatchMutiEntitiesException;
}
