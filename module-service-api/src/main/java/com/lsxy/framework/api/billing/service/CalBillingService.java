package com.lsxy.framework.api.billing.service;

import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.yunhuni.api.statistics.model.DayStatics;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/30.
 */
public interface CalBillingService {
    int AMOUNT_REDIS_MULTIPLE = 10000;//增量型的金额存在redis中的倍数关系（增量型的金额是以long形式存在redis中）
    String BILLING_DAY_PREFIX = "billing_day"; //当天账单key
    String USE_BALANCE_FIELD = "ubalance";     //当天消费
    String ADD_BALANCE_FIELD = "abalance";     //当天充值
    String USE_CONFERENCE_FIELD = "uconference";     //会议本日使用量
    String ADD_CONFERENCE_FIELD = "aconference";   //会议本日购买增加量
    String USE_VOICE_FIELD = "uvoice";             //语音本日使用量
    String ADD_VOICE_FIELD = "avoice";              //语音本日购买增加量
    String USE_SMS_FIELD = "usms";                 //短信本日使用量
    String ADD_SMS_FIELD = "asms";             //短信本日购买增加量
    String ADD_FSIZE_FIELD = "afsize";             //容量本日增加量（包括删除文件）
    String USE_FSIZE_FIELD = "ufsize";             //容量本日使用量
    String CALL_CONNECT_FIELD = "callconnect";             //本日接通量
    String CALL_SUM_FIELD = "callsum";             //本日总通话量
    String CALL_COST_TIME_FIELD = "callcosttime";             //本日通话时长

    /**
     * 获取余额
     * @param tenantId
     * @return
     */
    BigDecimal getBalance(String tenantId);

    /**
     * 从原始表中进行账务金额结算
     * @param tenantId 租户ID
     * @param lastBalanceDate 上次结算时间
     * @param balanceDate 本次结算时间
     * @param lastBalance 上次结算金额
     * @return
     */
    BigDecimal getBalance(String tenantId,Date lastBalanceDate,Date balanceDate,BigDecimal lastBalance);

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


    /**
     * 获取会议余量
     * @param tenantId
     * @return
     */
    Long getConference(String tenantId);

    /**
     * redis中的会议使用量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param time 时长（秒）
     */
    void incUseConference(String tenantId, Date date, Long time);

    /**
     * redis中的会议购买量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param time 时长（秒）
     */
    void incAddConference(String tenantId, Date date, Long time);


    /**
     * 获取语音余量
     * @param tenantId
     * @return
     */
    Long getVoice(String tenantId);

    /**
     * redis中的语音使用量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param time 时长（秒）
     */
    void incUseVoice(String tenantId, Date date, Long time);

    /**
     * redis中的语音购买量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param time 时长（秒）
     */
    void incAddVoice(String tenantId, Date date, Long time);

    /**
     * 获取短信余量
     * @param tenantId
     * @return
     */
    Long getSms(String tenantId);

    /**
     * redis中的短信使用量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param num 条
     */
    void incUseSms(String tenantId, Date date, Long num);

    /**
     * redis中的短信购买量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param num 条
     */
    void incAddSms(String tenantId, Date date, Long num);


    /**
     * 获取剩余容量
     * @param tenantId
     * @return
     */
    Long getFsize(String tenantId);

    /**
     * redis中的剩余容量使用量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param size byte
     */
    void incUseFsize(String tenantId, Date date, Long size);

    /**
     * redis中的剩余容量增加
     * @param tenantId 租户ID
     * @param date 日期
     * @param size byte
     */
    void incAddFsize(String tenantId, Date date, Long size);


    /**
     * 获取计算后的实时账务
     * @param tenantId
     * @return
     */
    Billing getCalBilling(String tenantId);

    /**
     * 获取当天充值总额
     * @param tenantId
     * @param date
     * @return
     */
    BigDecimal getAddBalancByDate(String tenantId,Date date);


    /**
     * 获取当天消费总额
     * @param tenantId
     * @param data
     * @return
     */
    BigDecimal getUseBalanceByDate(String tenantId,Date data);

    /**
     * 增加接通次数
     * @param tenantId
     * @param date
     */
    void incCallConnect(String tenantId,Date date);

    /**
     * 获取当天总接通次数
     * @param tenantId
     * @param date
     * @return
     */
    Long getCallConnectByDate(String tenantId,Date date);

    /**
     * 增加当日拨打量
     * @param tenantId
     * @param date
     * @return
     */
    void incCallSum(String tenantId,Date date);

    /**
     * 获取当天拨打量
     * @param tenantId
     * @param date
     * @return
     */
    Long getCallSumByDate(String tenantId,Date date);

    /**
     * 增加总消费时长
     * @param tenantId
     * @param date
     */
    void incCallCostTime(String tenantId,Date date,Long callCostTime);

    /**
     * 获取当天消费时长
     * @param tenantId
     * @return
     */
    Long getCallCostTimeByDate(String tenantId,Date date);

    /**
     * 获取当前的统计总量数据
     * @return
     */
    DayStatics getCurrentStatics(String tenantId);

    /**
     * 获取当天的统计增量
     * @param tenantId
     * @return
     */
    DayStatics getIncStaticsOfCurrentDay(String tenantId);

    /**
     * 获取当前月的统计增量
     * @param tenantId
     * @return
     */
    DayStatics getIncStaticsOfCurrentMonth(String tenantId);


    /**
     * 每日统计账务余额
     * @param date 统计日期
     */
    void calBilling(Date date);
}
