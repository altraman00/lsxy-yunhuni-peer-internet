package com.lsxy.framework.billing.service;

import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.BillingService;
import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.cache.manager.RedisCacheService;
import com.lsxy.framework.core.utils.BeanUtils;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.JSONUtil;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.recharge.service.RechargeService;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;
import com.lsxy.yunhuni.api.statistics.service.DayStaticsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/30.
 */
@Service
@Transactional
public class CalBillingServiceImpl implements CalBillingService{
    public static final Logger logger = LoggerFactory.getLogger(CalBillingServiceImpl.class);
    @Autowired
    BillingService billingService;
    @Autowired
    private RedisCacheService redisCacheService;
    @Autowired
    TenantService tenantService;
    @Autowired
    RechargeService rechargeService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    DayStaticsService dayStaticsService;
    /*
        余额
    */
    @Override
    public BigDecimal getBalance(String tenantId) {
        if(logger.isDebugEnabled()){
            logger.info("获取余额,tenantId:{}",tenantId);
        }
        BigDecimal balance;
        Date date = new Date();
        Date preDate = DateUtils.getPreDate(date);
        Billing billing = billingService.findBillingByTenantId(tenantId);
        if(billing == null){
            throw new RuntimeException("用户账务表不存在");
        }
        if(logger.isDebugEnabled()){
            logger.info("用户账务表：{}", JSONUtil.objectToJson(billing));
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

    @Override
    public BigDecimal getBalance(String tenantId, Date lastBalanceDate, Date balanceDate, BigDecimal lastBalance) {
        Date startDate = DateUtils.nextDate(lastBalanceDate);
        Date endDate = DateUtils.nextDate(balanceDate);
        BigDecimal recharge = rechargeService.getRechargeByTenantIdAndDate(tenantId,startDate,endDate);
        BigDecimal consume = consumeService.getConsumeByTenantIdAndDate(tenantId,startDate,endDate);
        return lastBalance.add(recharge).subtract(consume);
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
        return getIncrAmount(tenantId,date, USE_BALANCE_FIELD);
    }

    /**
     * 从redis中获取当天的充值
     * @param tenantId
     * @param date
     * @return
     */
    private BigDecimal getRecharge(String tenantId, Date date){
        return getIncrAmount(tenantId,date, ADD_BALANCE_FIELD);
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
        String key = BILLING_DAY_PREFIX + "_" + tenantId + "_" + dateStr;
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        String rechargeStr = (String) hashOps.get(type);
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
        incAmount(tenantId,date,amount, ADD_BALANCE_FIELD);
    }

    @Override
    public void incConsume(String tenantId,Date date,BigDecimal amount){
        incAmount(tenantId,date,amount, USE_BALANCE_FIELD);
    }

    /**
     * redis中的金额增量增加(充值或消费)
     * @param tenantId 租户ID
     * @param date 日期
     * @param amount 金额
     * @param type 类型（充值或消费的key前缀）
     */
    private void incAmount(String tenantId,Date date,BigDecimal amount,String type){
        if(amount == null){
            return;
        }
        //以long型存金额的增量，真实金额=redis中的金额/10000
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String key = BILLING_DAY_PREFIX + "_" + tenantId + "_" + dateStr;
        long l = amount.multiply(new BigDecimal(AMOUNT_REDIS_MULTIPLE)).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Long result = hashOps.increment(type, l);
        if(result == l){
            redisCacheService.expire(key,5 * 24 * 60 * 60);
        }
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
        return getIncrLong(tenantId,date, USE_CONFERENCE_FIELD);
    }

    /**
     * 从redis中获取当天的会议购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddConference(String tenantId, Date date){
        return getIncrLong(tenantId,date, ADD_CONFERENCE_FIELD);
    }

    /**
     * 从redis中获取增量（购买或消费）
     * @param tenantId
     * @param date
     * @param type 类型（购买或消费的key前缀）
     * @return 时长（秒）或容量Byte
     */
    private Long getIncrLong(String tenantId, Date date,String type){
        Long time;
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String key = BILLING_DAY_PREFIX + "_" + tenantId + "_" + dateStr;
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        String incrLongStr = (String) hashOps.get(type);
        if(StringUtils.isBlank(incrLongStr)){
            time = 0L;
        }else{
            time = Long.parseLong(incrLongStr);
        }
        return time;
    }

    @Override
    public void incAddConference(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, ADD_CONFERENCE_FIELD);
    }

    @Override
    public void incUseConference(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, USE_CONFERENCE_FIELD);
    }

    /**
     * redis中的增量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param l 时长（秒）或数量（条数）
     * @param type 类型（购买或消费的key前缀）
     */
    private void incLong(String tenantId,Date date,Long l,String type){
        if(l == null){
            return;
        }
        String dateStr = DateUtils.getTime(date,"yyyyMMdd");
        String key = BILLING_DAY_PREFIX + "_" + tenantId + "_" + dateStr;
        BoundHashOperations hashOps = redisCacheService.getHashOps(key);
        Long result = hashOps.increment(type, l);
        if(result == l){
            redisCacheService.expire(key,5 * 24 * 60 * 60);
        }
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
        return getIncrLong(tenantId,date, USE_VOICE_FIELD);
    }

    /**
     * 从redis中获取当天的语音购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddVoice(String tenantId, Date date){
        return getIncrLong(tenantId,date, ADD_VOICE_FIELD);
    }

    @Override
    public void incAddVoice(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, ADD_VOICE_FIELD);
    }

    @Override
    public void incUseVoice(String tenantId,Date date,Long time){
        incLong(tenantId,date,time, USE_VOICE_FIELD);
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
        Long useSms = getUseSms(tenantId, date);
        Long addSms = getAddSms(tenantId, date);
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
        return getIncrLong(tenantId,date, USE_SMS_FIELD);
    }

    /**
     * 从redis中获取当天的短信购买量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddSms(String tenantId, Date date){
        return getIncrLong(tenantId,date, ADD_SMS_FIELD);
    }

    @Override
    public void incAddSms(String tenantId,Date date,Long num){
        incLong(tenantId,date,num, ADD_SMS_FIELD);
    }


    @Override
    public void incUseSms(String tenantId,Date date,Long num){
        incLong(tenantId,date,num, USE_SMS_FIELD);
    }


    /*
        容量剩余
     */

    @Override
    public Long getFsize(String tenantId) {
        Long size;
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
            size = getFsizeByPreDateSum(tenantId, date, billing.getFileRemainSize());
        }else{
            //前日结算
            size = getFsizeByPrePreDateSum(tenantId, date, billing.getFileRemainSize());
        }

        return size;
    }

    /**
     * 获取容量剩余（昨日结算+昨日增加-昨日消费）
     * @param tenantId
     * @param date
     * @param fileRemainSize
     * @return
     */
    private Long getFsizeByPreDateSum(String tenantId, Date date, Long fileRemainSize) {
        Long useSize = getUseFsize(tenantId, date);
        Long addSize = getAddFsize(tenantId, date);
        return fileRemainSize + addSize - useSize;
    }

    /**
     * 获取容量剩余（前日结算+前日增加-前日消费+昨日增加-昨日消费）
     * @param tenantId
     * @param date
     * @param fileRemainSize
     * @return
     */
    private Long getFsizeByPrePreDateSum(String tenantId, Date date, Long fileRemainSize) {
        Long useSize = getUseFsize(tenantId, date);
        Long addSize = getAddFsize(tenantId, date);
        Date preDate = DateUtils.getPreDate(date);
        Long preUseSize = getUseFsize(tenantId, preDate);
        Long preAddSize = getAddFsize(tenantId, preDate);
        return fileRemainSize + addSize - useSize + preAddSize - preUseSize;
    }


    /**
     * 从redis中获取当天的容量使用量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getUseFsize(String tenantId, Date date){
        return getIncrLong(tenantId,date, USE_FSIZE_FIELD);
    }

    /**
     * 从redis中获取当天的容量增加量
     * @param tenantId
     * @param date
     * @return
     */
    private Long getAddFsize(String tenantId, Date date){
        return getIncrLong(tenantId,date, ADD_FSIZE_FIELD);
    }

    @Override
    public void incAddFsize(String tenantId, Date date, Long size){
        incLong(tenantId,date,size, ADD_FSIZE_FIELD);
    }


    @Override
    public void incUseFsize(String tenantId, Date date, Long size){
        incLong(tenantId,date,size, USE_FSIZE_FIELD);
    }


    @Override
    public Billing getCalBilling(String tenantId) {
        Billing billingOrg = billingService.findBillingByTenantId(tenantId);
        Billing billing = new Billing();
        try {
            BeanUtils.copyProperties(billing,billingOrg);
        } catch (Exception e) {
        }
        billing.setBalance(this.getBalance(tenantId));
        billing.setSmsRemain(this.getSms(tenantId));
        billing.setConferenceRemain(this.getConference(tenantId));
        billing.setVoiceRemain(this.getVoice(tenantId));
        //TODO 从redis 中取出剩余空间
        billing.setFileRemainSize(this.getFsize(tenantId));
        return billing;
    }


    @Override
    public BigDecimal getAddBalancByDate(String tenantId, Date date) {
        return getRecharge(tenantId,date);
    }


    @Override
    public BigDecimal getUseBalanceByDate(String tenantId, Date date) {
        return getConsume(tenantId,date);
    }


    @Override
    public void incCallConnect(String tenantId, Date date) {
        incLong(tenantId,date,1L, CALL_CONNECT_FIELD);
    }

    @Override
    public Long getCallConnectByDate(String tenantId, Date date) {
        return getIncrLong(tenantId,date, CALL_CONNECT_FIELD);
    }


    @Override
    public void incCallSum(String tenantId, Date date) {
        incLong(tenantId,date,1L, CALL_SUM_FIELD);
    }

    @Override
    public Long getCallSumByDate(String tenantId, Date date) {
        return getIncrLong(tenantId,date, CALL_SUM_FIELD);
    }

    @Override
    public void incCallCostTime(String tenantId, Date date,Long costTimeLong) {
        incLong(tenantId,date,costTimeLong, CALL_COST_TIME_FIELD);
    }

    @Override
    public Long getCallCostTimeByDate(String tenantId, Date date) {
        return getIncrLong(tenantId,date, CALL_COST_TIME_FIELD);
    }

    @Override
    public DayStatics getCurrentStatics(String tenantId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date preDate = DateUtils.getPreDate(date);
        DayStatics dayStatics = dayStaticsService.getStaticByTenantId(tenantId,preDate);
        if(dayStatics == null){
            dayStatics = new DayStatics(tenantId,null,date,getAddBalancByDate(tenantId,date),getUseBalanceByDate(tenantId,date),
                    getCallConnectByDate(tenantId,date),getCallSumByDate(tenantId,date),getCallCostTimeByDate(tenantId,date));
        }else{
            dayStatics = getCurrentDayStatics(dayStatics,date);
        }
        return dayStatics;
    }


    private DayStatics getCurrentDayStatics(DayStatics dayStatics, Date date) {
        Date dt = dayStatics.getDt();
        if(dt.getTime() < date.getTime()){
            String tenantId = dayStatics.getTenantId();
            Date nextDate = DateUtils.nextDate(dt);
            DayStatics nextDayStatics = new DayStatics(tenantId,null,nextDate,
                    dayStatics.getRecharge().add(getAddBalancByDate(tenantId,nextDate)),
                    dayStatics.getConsume().add(getUseBalanceByDate(tenantId,nextDate)),
                    dayStatics.getCallConnect() + getCallConnectByDate(tenantId,nextDate),
                    dayStatics.getCallSum() + getCallSumByDate(tenantId,nextDate),
                    dayStatics.getCallCostTime() + getCallCostTimeByDate(tenantId,nextDate));
            return getCurrentDayStatics(nextDayStatics, date);
        }else{
            return dayStatics;
        }
    }

    @Override
    public DayStatics getIncStaticsOfCurrentDay(String tenantId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        DayStatics dayStatics = new DayStatics(tenantId,null,date,getAddBalancByDate(tenantId,date),getUseBalanceByDate(tenantId,date),
                getCallConnectByDate(tenantId,date),getCallSumByDate(tenantId,date),getCallCostTimeByDate(tenantId,date));
        return dayStatics;
    }

    @Override
    public DayStatics getIncStaticsOfCurrentMonth(String tenantId) {
        Date date = new Date();
        String dateStr = DateUtils.formatDate(date, "yyyyMMdd");
        date = DateUtils.parseDate(dateStr,"yyyyMMdd");
        Date firstTimeOfMonth = DateUtils.getFirstTimeOfMonth(date);
        DayStatics result;
        if(date.equals(firstTimeOfMonth)){
            result = getIncStaticsOfCurrentDay(tenantId);
        }else{
            DayStatics currentStatics = getCurrentStatics(tenantId);
            Date preMonthDate = DateUtils.getPreDate(firstTimeOfMonth);
            DayStatics preMonthDayStatics = dayStaticsService.getStaticByTenantId(tenantId,preMonthDate);
            if(currentStatics != null && preMonthDayStatics != null){
                preMonthDayStatics = getCurrentDayStatics(preMonthDayStatics,preMonthDate);
                result = new DayStatics(tenantId,null,date,currentStatics.getRecharge().subtract(preMonthDayStatics.getRecharge()),
                        currentStatics.getConsume().subtract(preMonthDayStatics.getConsume()),
                        currentStatics.getCallConnect() - preMonthDayStatics.getCallConnect(),
                        currentStatics.getCallSum() - preMonthDayStatics.getCallSum(),
                        currentStatics.getCallCostTime() - preMonthDayStatics.getCallCostTime());
            }else{
                result = getIncStaticsOfCurrentDay(tenantId);
            }
        }
        return result;
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
                BigDecimal balance = this.getBalance(tenantId, balanceDate,date, billing.getBalance());
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
                //TODO 统计容量
                Long size = this.getFsizeByPreDateSum(tenantId, date, billing.getFileRemainSize());
                billing.setFileRemainSize(size);
                //更新结算时间
                billing.setBalanceDate(date);
                billingService.save(billing);
            }
        }
    }


}
