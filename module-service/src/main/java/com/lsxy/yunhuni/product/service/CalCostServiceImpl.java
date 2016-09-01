package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.consume.model.CaptchaUse;
import com.lsxy.framework.api.consume.model.Consume;
import com.lsxy.framework.api.consume.model.VoiceTimeUse;
import com.lsxy.framework.api.consume.service.CaptchaUseService;
import com.lsxy.framework.api.consume.service.ConsumeService;
import com.lsxy.framework.api.consume.service.VoiceTimeUseService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.billing.service.CalBillingService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import com.lsxy.yunhuni.api.product.service.ProductPriceService;
import com.lsxy.yunhuni.api.product.service.ProductService;
import com.lsxy.yunhuni.api.product.service.ProductTenantDiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class CalCostServiceImpl implements CalCostService{
    @Autowired
    ProductService productService;
    @Autowired
    ProductPriceService productPriceService;
    @Autowired
    ProductTenantDiscountService productTenantDiscountService;
    @Autowired
    CalBillingService calBillingService;
    @Autowired
    ConsumeService consumeService;
    @Autowired
    CaptchaUseService captchaUseService;
    @Autowired
    VoiceTimeUseService voiceTimeUseService;

    @Override
    public BigDecimal calCost(Product product, String tenantId, Long time) {
        BigDecimal cost;
        ProductPrice productPrice = productPriceService.getAvailableProductPrice(product.getId());
        Double discount = productTenantDiscountService.getDiscountByProductIdAndTenantId(product.getId(), tenantId);
        if(product.getCalType() == Product.CAL_TYPE_NUM){
            //如果是计量，则只需单价*折扣
            cost = productPrice.getPrice().multiply(new BigDecimal(Double.toString(discount)));
        }else{
            //如果是计时，则只需 时长单位数量*单价*折扣
            BigDecimal calNum = new BigDecimal(time).divide(new BigDecimal(product.getTimeUnit()), 0, BigDecimal.ROUND_UP);
            cost = calNum.multiply(productPrice.getPrice()).multiply(new BigDecimal(Double.toString(discount))).setScale(4,BigDecimal.ROUND_HALF_UP);
        }
        return cost;
    }

    @Override
    public void consume(String apiCmd, String tenantId, String appId, Long time, Date dt) {
        ProductCode productCode = ProductCode.changeApiCmdToProductCode(apiCmd);
        Product product = productService.getProductByCode(productCode.name());
        switch (productCode){
            case captcha_call:{
                //短信
                Long sms = calBillingService.getSms(tenantId);
                if(sms >0){
                    calBillingService.incUseSms(tenantId,dt,1L);
                    CaptchaUse captchaUse = new CaptchaUse(dt,productCode.name(),appId,tenantId);
                    captchaUseService.save(captchaUse);
                }else{
                    insertConsume(tenantId, appId, time, dt, productCode.name(),productCode.getRemark(), product);
                }
                break;
            }
            case sys_conf:{
                //会议
                Long useTime = calUnitNum(time, product) * product.getTimeUnit();
                Long conference = calBillingService.getConference(tenantId);
                if(useTime <= conference){
                    calBillingService.incUseConference(tenantId,dt,useTime);
                    VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,useTime,product.getTimeUnit(),product.getUnit(),appId,tenantId);
                    voiceTimeUseService.save(use);
                }else if(conference > 0){
                    //先扣量
                    calBillingService.incUseConference(tenantId,dt,conference);
                    VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,conference,product.getTimeUnit(),product.getUnit(),appId,tenantId);
                    voiceTimeUseService.save(use);
                    //再扣费
                    insertConsume(tenantId, appId, useTime - conference, dt, productCode.name(),productCode.getRemark(), product);
                } else{
                    insertConsume(tenantId, appId, time, dt, productCode.name(),productCode.getRemark(), product);
                }
                break;
            }
            default:{
                Long useTime = calUnitNum(time, product) * product.getTimeUnit();
                //语音
                Long voice = calBillingService.getVoice(tenantId);
                if(useTime <= voice){
                    calBillingService.incUseVoice(tenantId,dt,useTime);
                    VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,useTime,product.getTimeUnit(),product.getUnit(),appId,tenantId);
                    voiceTimeUseService.save(use);
                }else if(voice > 0){
                    //先扣量
                    calBillingService.incUseVoice(tenantId,dt,voice);
                    VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,voice,product.getTimeUnit(),product.getUnit(),appId,tenantId);
                    voiceTimeUseService.save(use);
                    //再扣费
                    insertConsume(tenantId, appId, useTime - voice, dt, productCode.name(),productCode.getRemark(), product);
                } else{
                    insertConsume(tenantId, appId, time, dt, productCode.name(),productCode.getRemark(), product);
                }
                break;
            }
        }

    }

    @Override
    public boolean isCallTimeRemainOrBalanceEnough(String apiCmd, String tenantId) {
        ProductCode productCode = ProductCode.changeApiCmdToProductCode(apiCmd);
        switch(productCode){
            case captcha_call:{
                Long sms = calBillingService.getSms(tenantId);
                if(sms > 0){
                    return true;
                }else{
                    return isBalanceEnough(tenantId);
                }
            }
            case conf_call:{
                Long conference = calBillingService.getConference(tenantId);
                if(conference > 0){
                    return true;
                }else{
                    return isBalanceEnough(tenantId);
                }
            }
            default:{
                Long voice = calBillingService.getVoice(tenantId);
                if(voice > 0){
                    return true;
                }else{
                    return isBalanceEnough(tenantId);
                }
            }
        }
    }

    /**
     * 余额是否大于标准线
     * @param tenantId
     * @return
     */
    private boolean isBalanceEnough(String tenantId) {
        BigDecimal balance = calBillingService.getBalance(tenantId);
        //TODO 余额是否充足标准线
        if(balance.compareTo(new BigDecimal(0)) == 1){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 插入消费表
     * @param tenantId
     * @param appId
     * @param time
     * @param dt
     * @param code
     * @param remark
     * @param product
     */
    private void insertConsume(String tenantId, String appId, Long time, Date dt, String code, String remark,Product product) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        BigDecimal cost = this.calCost(product, tenantId, time);
        calBillingService.incConsume(tenantId,dt,cost);
        Consume consume = new Consume(dt,code,cost,remark,appId,tenant);
        consumeService.save(consume);
    }

    /**
     * 多少个时长单位，时长/时长单位（不满一个时长单位按一个时长单位算)
     * @param time
     * @param product
     * @return
     */
    private Long calUnitNum(Long time, Product product) {
        return new BigDecimal(time).divide(new BigDecimal(product.getTimeUnit()),0,BigDecimal.ROUND_UP).longValue();
    }

}
