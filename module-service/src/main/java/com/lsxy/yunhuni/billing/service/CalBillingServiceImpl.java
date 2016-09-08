package com.lsxy.yunhuni.billing.service;

import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.yunhuni.api.billing.model.Billing;
import com.lsxy.yunhuni.api.billing.service.BillingService;
import com.lsxy.yunhuni.api.billing.service.CalBillingService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/30.
 */
@Service
public class CalBillingServiceImpl implements CalBillingService{

    @Autowired
    BillingService billingService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    TenantService tenantService;

    /*
        余额
    */
    @Override
    public BigDecimal getBalance(String tenantId) {
        BigDecimal balance;
        Date date = new Date();
        Date preDate = DateUtils.getPreDate(date);
        Billing billing = billingService.findBillingByTenantId(tenantId);
        if(billing == null){
            throw new RuntimeException("用户账务表不存在");
        }
        Date balanceDate = billing.getBalanceDate();
        String balanceDateStr = null;
        if(balanceDate != null){
            balanceDateStr = DateUtils.formatDate(balanceDate, "yyyyMMdd");
        }
        String preDateStr = DateUtils.formatDate(preDate, "yyyyMMdd");
        if(preDateStr.equals(balanceDateStr)){
            //昨日结算
            balance = getBalanceByPreDateSum(tenantId, date, billing.getBalance());
        }else{
            //前日结算
            balance = getBalanceByPrePreDateSum(tenantId, date, billing.getBalance());
        }

        return balance;
    }

    /**
     * 获取余额（结算+充值-消费）
     * @param tenantId
     * @param date
     * @param sumBalance
     * @return
     */
    private BigDecimal getBalanceByPreDateSum(String tenantId, Date date, BigDecimal sumBalance) {
        BigDecimal consume = getConsume(tenantId, date);
        BigDecimal recharge = getRecharge(tenantId, date);
        return sumBalance.add(recharge).subtract(consume);
    }

    /**
     * 获取余额（结算+充值-消费+前一天充值-前一天消费）
     * @param tenantId
     * @param date
     * @param sumBalance
     * @return
     */
    private BigDecimal getBalanceByPrePreDateSum(String tenantId, Date date, BigDecimal sumBalance) {
        BigDecimal balance;
        BigDecimal consume = getConsume(tenantId, date);
        BigDecimal recharge = getRecharge(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        BigDecimal preDateConsume = getConsume(tenantId, preDate);
        BigDecimal preDateRecharge = getRecharge(tenantId, preDate);
        balance = sumBalance.add(recharge).subtract(consume).add(preDateRecharge).subtract(preDateConsume);
        return balance;
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


    /*
        会议
     */
    @Override
    public Long getConference(String tenantId) {
        Long conference;
        Date date = new Date();
        Billing billing = billingService.findBillingByTenantId(tenantId);
        if(billing == null){
            throw new RuntimeException("用户账务表不存在");
        }
        Date balanceDate = billing.getBalanceDate();
        String balanceDateStr = null;
        if(balanceDate != null){
            DateUtils.formatDate(balanceDate, "yyyyMMdd");
        }
        Date preDate = DateUtils.getPreDate(date);
        String preDateStr = DateUtils.formatDate(preDate, "yyyyMMdd");
        if(preDateStr.equals(balanceDateStr)){
            //昨日结算
            conference = getConferenceByPreDateSum(tenantId, date, billing.getConferenceRemain());
        }else{
            //前日结算
            conference = getConferenceByPrePreDateSum(tenantId, date, billing.getConferenceRemain());
        }

        return conference;
    }

    /**
     * 获取会议余量（昨日结算+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumConference
     * @return
     */
    private Long getConferenceByPreDateSum(String tenantId, Date date, Long sumConference) {
        Long useConference = getUseConference(tenantId, date);
        Long addConference = getAddConference(tenantId, date);
        return sumConference + addConference - useConference;
    }

    /**
     * 获取会议余量（前日结算+前日购买-前日消费+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumConference
     * @return
     */
    private Long getConferenceByPrePreDateSum(String tenantId, Date date, Long sumConference) {
        Long useConference = getUseConference(tenantId, date);
        Long addConference = getAddConference(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        Long preUseConference = getUseConference(tenantId, preDate);
        Long preAddConference = getAddConference(tenantId, preDate);
        return sumConference + addConference - useConference + preAddConference - preUseConference;
    }


    /**
     * 从redis中获取当天的会议使用量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getUseConference(String tenantId, Date date){
        return getIncrLong(tenantId,date, USE_CONFERENCE_PREFIX);
    }

    /**
     * 从redis中获取当天的会议购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddConference(String tenantId, Date date){
        return getIncrLong(tenantId,date,ADD_CONFERENCE_PREFIX);
    }

    /**
     * 从redis中获取增量（购买或消费）
     * @param tenantId
     * @param date
     * @param type 类型（购买或消费的key前缀）
     * @return 时长（秒）
     */
    private Long getIncrLong(String tenantId, Date date,String type){
        Long time;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String conferenceStr = redisCacheService.get(type + "_" + tenantId + "_" + dateStr);
        if(StringUtils.isBlank(conferenceStr)){
            time = 0L;
        }else{
            time = Long.parseLong(conferenceStr);
        }
        return time;
    }

    @Override
    public void incAddConference(String tenantId,Date date,Long time){
        incLong(tenantId,date,time,ADD_CONFERENCE_PREFIX);
    }

    @Override
    public void incUseConference(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, USE_CONFERENCE_PREFIX);
    }

    /**
     * redis中的增量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param l 时长（秒）或数量（条数）
     * @param type 类型（购买或消费的key前缀）
     */
    private void incLong(String tenantId,Date date,Long l,String type){
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        redisCacheService.incrBy(type + "_" + tenantId + "_" + dateStr, l);
    }

    /*
        语音剩余
     */

    @Override
    public Long getVoice(String tenantId) {
        Long voice;
        Date date = new Date();
        Billing billing = billingService.findBillingByTenantId(tenantId);
        if(billing == null){
            throw new RuntimeException("用户账务表不存在");
        }
        Date balanceDate = billing.getBalanceDate();
        String balanceDateStr = null;
        if(balanceDate != null){
            DateUtils.formatDate(balanceDate, "yyyyMMdd");
        }
        Date preDate = DateUtils.getPreDate(date);
        String preDateStr = DateUtils.formatDate(preDate, "yyyyMMdd");
        if(preDateStr.equals(balanceDateStr)){
            //昨日结算
            voice = getVoiceByPreDateSum(tenantId, date, billing.getVoiceRemain());
        }else{
            //前日结算
            voice = getVoiceByPrePreDateSum(tenantId, date, billing.getVoiceRemain());
        }

        return voice;
    }

    /**
     * 获取语音余量（昨日结算+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumVoice
     * @return
     */
    private Long getVoiceByPreDateSum(String tenantId, Date date, Long sumVoice) {
        Long useVoice = getUseVoice(tenantId, date);
        Long addVoice = getAddVoice(tenantId, date);
        return sumVoice + addVoice - useVoice;
    }

    /**
     * 获取语音余量（前日结算+前日购买-前日消费+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumVoice
     * @return
     */
    private Long getVoiceByPrePreDateSum(String tenantId, Date date, Long sumVoice) {
        Long useVoice = getUseVoice(tenantId, date);
        Long addVoice = getAddVoice(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        Long preUseVoice = getUseVoice(tenantId, preDate);
        Long preAddVoice = getAddVoice(tenantId, preDate);
        return sumVoice + addVoice - useVoice + preAddVoice - preUseVoice;
    }


    /**
     * 从redis中获取当天的语音使用量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getUseVoice(String tenantId, Date date){
        return getIncrLong(tenantId,date,USE_VOICE_PREFIX);
    }

    /**
     * 从redis中获取当天的语音购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddVoice(String tenantId, Date date){
        return getIncrLong(tenantId,date,ADD_VOICE_PREFIX);
    }

    @Override
    public void incAddVoice(String tenantId,Date date,Long time){
        incLong(tenantId,date,time,ADD_VOICE_PREFIX);
    }

    @Override
    public void incUseVoice(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, USE_VOICE_PREFIX);
    }


    /*
        短信剩余
     */

    @Override
    public Long getSms(String tenantId) {
        Long sms;
        Date date = new Date();
        Billing billing = billingService.findBillingByTenantId(tenantId);
        if(billing == null){
            throw new RuntimeException("用户账务表不存在");
        }
        Date balanceDate = billing.getBalanceDate();
        String balanceDateStr = null;
        if(balanceDate != null){
            DateUtils.formatDate(balanceDate, "yyyyMMdd");
        }
        Date preDate = DateUtils.getPreDate(date);
        String preDateStr = DateUtils.formatDate(preDate, "yyyyMMdd");
        if(preDateStr.equals(balanceDateStr)){
            //昨日结算
            sms = getSmsByPreDateSum(tenantId, date, billing.getSmsRemain());
        }else{
            //前日结算
            sms = getSmsByPrePreDateSum(tenantId, date, billing.getSmsRemain());
        }

        return sms;
    }

    /**
     * 获取短信余量（昨日结算+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumSms
     * @return
     */
    private Long getSmsByPreDateSum(String tenantId, Date date, Long sumSms) {
        Long useSms = getUseVoice(tenantId, date);
        Long addSms = getAddVoice(tenantId, date);
        return sumSms + addSms - useSms;
    }

    /**
     * 获取短信余量（前日结算+前日购买-前日消费+昨日购买-昨日消费）
     * @param tenantId
     * @param date
     * @param sumSms
     * @return
     */
    private Long getSmsByPrePreDateSum(String tenantId, Date date, Long sumSms) {
        Long useSms = getUseSms(tenantId, date);
        Long addSms = getAddSms(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        Long preUseSms = getUseSms(tenantId, preDate);
        Long preAddSms = getAddSms(tenantId, preDate);
        return sumSms + addSms - useSms + preAddSms - preUseSms;
    }


    /**
     * 从redis中获取当天的短信使用量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getUseSms(String tenantId, Date date){
        return getIncrLong(tenantId,date, USE_SMS_PREFIX);
    }

    /**
     * 从redis中获取当天的短信购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddSms(String tenantId, Date date){
        return getIncrLong(tenantId,date,ADD_SMS_PREFIX);
    }

    @Override
    public void incAddSms(String tenantId,Date date,Long num){
        incLong(tenantId,date,num,ADD_SMS_PREFIX);
    }


    @Override
    public void incUseSms(String tenantId,Date date,Long num){
        incLong(tenantId,date,num, USE_SMS_PREFIX);
    }

    @Override
    public Billing getCalBilling(String tenantId) {
        Billing billing = billingService.findBillingByTenantId(tenantId);
        billing.setBalance(this.getBalance(tenantId));
        billing.setSmsRemain(this.getSms(tenantId));
        billing.setConferenceRemain(this.getConference(tenantId));
        billing.setVoiceRemain(this.getVoice(tenantId));
        //TODO 从redis 中取出剩余空间
        return billing;
    }

    @Override
    public void calBilling(Date date) {
        String yyyyMMdd = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(yyyyMMdd,"yyyyMMdd");
        Iterable<Tenant> list = tenantService.list();
        for(Tenant tenant:list){
            String tenantId = tenant.getId();
            Billing billing = billingService.findBillingByTenantId(tenantId);
            Date balanceDate = billing.getBalanceDate();
            if(balanceDate.getTime() < date.getTime()){
                //TODO 统计余额
                BigDecimal balance = this.getBalanceByPreDateSum(tenantId, date, billing.getBalance());
                billing.setBalance(balance);
                //TODO 统计语音通知
                Long voice = this.getVoiceByPreDateSum(tenantId, date, billing.getVoiceRemain());
                billing.setVoiceRemain(voice);
                //TODO 统计会议
                Long conference = this.getConferenceByPreDateSum(tenantId, date, billing.getConferenceRemain());
                billing.setConferenceRemain(conference);
                //TODO 统计短信
                Long sms = this.getSmsByPreDateSum(tenantId, date, billing.getSmsRemain());
                billing.setSmsRemain(sms);
                //更新结算时间
                billing.setBalanceDate(date);
                billingService.save(billing);
            }
        }
    }

}
