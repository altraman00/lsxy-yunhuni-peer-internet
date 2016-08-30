package com.lsxy.yunhuni.api.billing.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.yunhuni.api.billing.model.Billing;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 账务相关接口
 * Created by liups on 2016/6/28.
 */
public interface BillingService extends BaseService<Billing> {
    int AMOUNT_REDIS_MULTIPLE = 10000;//增量型的金额存在redis中的倍数关系（增量型的金额是以long形式存在redis中）
    String REMAIN_BALANCE_PREFIX = "rbalance";  //当天余额
    String USE_BALANCE_PREFIX = "ubalance";     //当天消费
    String ADD_BALANCE_PREFIX = "abalance";     //当天充值

    /**
     * 根据用户名查找账务
     * @param username 用户名
     * @return 账务
     */
    Billing findBillingByUserName(String username);

    /**
     * 根据租户ID查找账务
     * @param tenantId 租户ID
     * @return 账务
     */
    Billing findBillingByTenantId(String tenantId);

    /**
     * 获取余额
     * @param tenantId
     * @return
     */
    BigDecimal getBalance(String tenantId);

    /**
     * 将余额存入redis中
     * @param tenantId
     * @param date
     * @param balance
     */
    String setBalanceToRedis(String tenantId, Date date, BigDecimal balance);

    /**
     * redis中的充值增量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param amount 金额
     */
    void incRecharge(String tenantId, Date date, BigDecimal amount);

    /**
     * redis中的充值增量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param amount 金额
     */
    void incConsume(String tenantId, Date date, BigDecimal amount);
}
