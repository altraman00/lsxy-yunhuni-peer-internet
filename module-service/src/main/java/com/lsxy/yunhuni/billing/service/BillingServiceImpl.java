package com.lsxy.yunhuni.billing.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.billing.dao.BillingDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/6/28.
 */
@Service
public class BillingServiceImpl extends AbstractService<Billing> implements BillingService {
    @Autowired
    private BillingDao billingDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private RedisCacheService redisCacheService;

    @Override
    public BaseDaoInterface<Billing, Serializable> getDao() {
        return billingDao;
    }
    @Override
    public Billing findBillingByUserName(String username){
        Billing billing = null;
        Tenant tenant = tenantService.findTenantByUserName(username);
        if(tenant != null){
            billing = findBillingByTenantId(tenant.getId());
        }
        return billing;
    }

    @Override
    public Billing findBillingByTenantId(String tenantId) {
        String hql = "from Billing obj where obj.tenant.id=?1";
        try {
            return this.findUnique(hql,tenantId);
        } catch (MatchMutiEntitiesException e) {
            e.printStackTrace();
            throw new RuntimeException("存在多个对应账务信息");
        }
    }

    @Override
    public BigDecimal getBalance(String tenantId) {
        BigDecimal balance;
        Date date = new Date();
        String balanceStr = this.getBalanceStr(tenantId,date);
        if(StringUtils.isNotBlank(balanceStr)){
            //昨日结算
            balance = getBalanceByPreDateSum(tenantId, date, balanceStr);
        }else{
            Date preDate = DateUtils.getPreDate(date);
            String preBalanceStr = this.getBalanceStr(tenantId, preDate);
            if(StringUtils.isNotBlank(preBalanceStr)){
                //前日结算
                balance = getBalanceByPrePreDateSum(tenantId, date, preBalanceStr);
            }else{
                Billing billing = this.findBillingByTenantId(tenantId);
                BigDecimal consume = this.getConsumeStr(tenantId, preDate);
                BigDecimal recharge = this.getRechargeStr(tenantId, preDate);
                if(consume.compareTo(new BigDecimal(0))==1 || recharge.compareTo(new BigDecimal(0))==1 ){
                    //前日结算
                    Date prePreDate = DateUtils.getPreDate(preDate);
                    preBalanceStr = this.setBalanceToRedis(tenantId, prePreDate, billing);
                    balance = getBalanceByPrePreDateSum(tenantId, date, preBalanceStr);
                }else{
                    //昨日结算
                    balanceStr = this.setBalanceToRedis(tenantId, preDate, billing);
                    balance = getBalanceByPreDateSum(tenantId, date, balanceStr);
                }
            }
        }

        return balance;
    }

    /**
     * 获取余额（昨日结算+昨日充值-昨日消费）
     * @param tenantId
     * @param date
     * @param balanceStr
     * @return
     */
    private BigDecimal getBalanceByPreDateSum(String tenantId, Date date, String balanceStr) {
        BigDecimal balance;
        BigDecimal consume = getConsumeStr(tenantId, date);
        BigDecimal recharge = getRechargeStr(tenantId, date);
        balance = new BigDecimal(balanceStr).add(recharge).subtract(consume);
        return balance;
    }

    /**
     * 获取余额（前日结算+前日充值-前日消费+昨日充值-昨日消费）
     * @param tenantId
     * @param date
     * @param balanceStr
     * @return
     */
    private BigDecimal getBalanceByPrePreDateSum(String tenantId, Date date, String balanceStr) {
        BigDecimal balance;
        BigDecimal consume = getConsumeStr(tenantId, date);
        BigDecimal recharge = getRechargeStr(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        BigDecimal preDateConsume = getConsumeStr(tenantId, preDate);
        BigDecimal preDateRecharge = getRechargeStr(tenantId, preDate);
        balance = new BigDecimal(balanceStr).add(recharge).subtract(consume).add(preDateRecharge).subtract(preDateConsume);
        return balance;
    }


    /**
     * 将余额存入redis中
     * @param tenantId
     * @param preDate
     * @param billing
     */
    private String setBalanceToRedis(String tenantId, Date preDate, Billing billing) {
        String balanceStr;
        String preDateStr = DateUtils.getTime(preDate,"yyyyMMdd");
        balanceStr = billing.getBalance().toString();
        redisCacheService.set("rbalance_" + tenantId + "_" + preDateStr,balanceStr,5*24*60*60);
        return balanceStr;
    }

    /**
     * 从redis中获取当天的消费
     * @param tenantId
     * @param date
     * @return
     */
    private BigDecimal getConsumeStr(String tenantId, Date date){
        BigDecimal consume;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String consumeStr = redisCacheService.get("ubalance_" + tenantId + "_" + dateStr);
        if(StringUtils.isBlank(consumeStr)){
            consume = new BigDecimal(0);
        }else{
            consume = new BigDecimal(consumeStr);
        }
        return consume;
    }

    /**
     * 从redis中获取当天的充值
     * @param tenantId
     * @param date
     * @return
     */
    private BigDecimal getRechargeStr(String tenantId, Date date){
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String rechargeStr = redisCacheService.get("abalance" + tenantId + "_" + dateStr);
        BigDecimal recharge;
        if(StringUtils.isBlank(rechargeStr)){
            recharge = new BigDecimal(0);
        }else{
            recharge = new BigDecimal(rechargeStr);
        }
        return recharge;
    }

    /**
     * 从redis中获取昨天的结算
     * @param tenantId
     * @param date
     * @return
     */
    private String getBalanceStr(String tenantId, Date date) {
        Date preDate = DateUtils.getPreDate(date);
        String preDateStr = DateUtils.getTime(preDate,"yyyyMMdd");
        String balanceStr = redisCacheService.get("rbalance_" + tenantId + "_" + preDateStr);
        return balanceStr;
    }


}
