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
                BigDecimal consume = this.getConsume(tenantId, preDate);
                BigDecimal recharge = this.getRecharge(tenantId, preDate);
                if(consume.compareTo(new BigDecimal(0))==1 || recharge.compareTo(new BigDecimal(0))==1 ){
                    //前日结算
                    Date prePreDate = DateUtils.getPreDate(preDate);
                    preBalanceStr = this.setBalanceToRedis(tenantId, prePreDate, billing.getBalance());
                    balance = getBalanceByPrePreDateSum(tenantId, date, preBalanceStr);
                }else{
                    //昨日结算
                    balanceStr = this.setBalanceToRedis(tenantId, preDate, billing.getBalance());
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
        BigDecimal consume = getConsume(tenantId, date);
        BigDecimal recharge = getRecharge(tenantId, date);
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
        BigDecimal consume = getConsume(tenantId, date);
        BigDecimal recharge = getRecharge(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        BigDecimal preDateConsume = getConsume(tenantId, preDate);
        BigDecimal preDateRecharge = getRecharge(tenantId, preDate);
        balance = new BigDecimal(balanceStr).add(recharge).subtract(consume).add(preDateRecharge).subtract(preDateConsume);
        return balance;
    }

    /**
     * 从redis中获取前一天的结算
     * @param tenantId
     * @param date
     * @return
     */
    private String getBalanceStr(String tenantId, Date date) {
        Date preDate = DateUtils.getPreDate(date);
        String preDateStr = DateUtils.getTime(preDate,"yyyyMMdd");
        String balanceStr = redisCacheService.get(REMAIN_BALANCE_PREFIX + "_" + tenantId + "_" + preDateStr);
        return balanceStr;
    }

    @Override
    public String setBalanceToRedis(String tenantId, Date date, BigDecimal balance) {
        String balanceStr;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        balanceStr = balance.toString();
        redisCacheService.set(REMAIN_BALANCE_PREFIX + "_"  + tenantId + "_" + dateStr,balanceStr,5*24*60*60);
        return balanceStr;
    }

    /**
     * 从redis中获取当天的消费
     * @param tenantId
     * @param date
     * @return
     */
    private BigDecimal getConsume(String tenantId, Date date){
        return getIncrAmount(tenantId,date,USE_BALANCE_PREFIX);
    }

    /**
     * 从redis中获取当天的充值
     * @param tenantId
     * @param date
     * @return
     */
    private BigDecimal getRecharge(String tenantId, Date date){
       return getIncrAmount(tenantId,date,ADD_BALANCE_PREFIX);
    }

    /**
     * 从redis中获取增量金额（充值或消费）
     * @param tenantId
     * @param date
     * @param type 类型（充值或消费的key前缀）
     * @return
     */
    private BigDecimal getIncrAmount(String tenantId, Date date,String type){
        BigDecimal incrAmount;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String rechargeStr = redisCacheService.get(type + "_" + tenantId + "_" + dateStr);
        if(StringUtils.isBlank(rechargeStr)){
            incrAmount = new BigDecimal(0);
        }else{
            //redis以long型存金额的增量，真实金额=redis中的金额/10000
            incrAmount = new BigDecimal(rechargeStr).divide(new BigDecimal(AMOUNT_REDIS_MULTIPLE));
        }
        return incrAmount;
    }


    @Override
    public void incRecharge(String tenantId,Date date,BigDecimal amount){
        incAmount(tenantId,date,amount,ADD_BALANCE_PREFIX);
    }


    @Override
    public void incConsume(String tenantId,Date date,BigDecimal amount){
        incAmount(tenantId,date,amount,USE_BALANCE_PREFIX);
    }

    /**
     * redis中的金额增量增加(充值或消费)
     * @param tenantId 租户ID
     * @param date 日期
     * @param amount 金额
     * @param type 类型（充值或消费的key前缀）
     */
    private void incAmount(String tenantId,Date date,BigDecimal amount,String type){
        //以long型存金额的增量，真实金额=redis中的金额/10000
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        long l = amount.multiply(new BigDecimal(AMOUNT_REDIS_MULTIPLE)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        redisCacheService.incrBy(type + "_" + tenantId + "_" + dateStr, l);
    }
}
