package com.lsxy.yunhuni.api.billing.service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/30.
 */
public interface CalBillingService {
    int AMOUNT_REDIS_MULTIPLE = 10000;//增量型的金额存在redis中的倍数关系（增量型的金额是以long形式存在redis中）
    String USE_BALANCE_PREFIX = "ubalance";     //当天消费
    String ADD_BALANCE_PREFIX = "abalance";     //当天充值
    String USE_CONFERENCE_PREFIX = "uconference";     //会议本日使用量
    String ADD_CONFERENCE_PREFIX = "aconference";   //会议本日购买增加量
    String USE_VOICE_PREFIX = "uvoice";             //语音本日使用量
    String ADD_VOICE_PREFIX = "avoice";              //语音本日购买增加量
    String USE_SMS_PREFIX = "usms";                 //短信本日使用量
    String ADD_SMS_PREFIX = "asms";             //短信本日购买增加量

    /**
     * 获取余额
     * @param tenantId
     * @return
     */
    BigDecimal getBalance(String tenantId);

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
}
