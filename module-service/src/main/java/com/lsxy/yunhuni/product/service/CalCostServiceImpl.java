package com.lsxy.yunhuni.product.service;

import com.lsxy.framework.api.billing.service.CalBillingService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.yunhuni.api.consume.model.VoiceTimeUse;
import com.lsxy.yunhuni.api.consume.service.CaptchaUseService;
import com.lsxy.yunhuni.api.consume.service.ConsumeService;
import com.lsxy.yunhuni.api.consume.service.VoiceTimeUseService;
import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.model.Product;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.product.model.ProductPrice;
import com.lsxy.yunhuni.api.product.service.*;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/27.
 */
@Service
public class CalCostServiceImpl implements CalCostService{
    private static final Logger logger = LoggerFactory.getLogger(CalCostServiceImpl.class);
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
    @Autowired
    ProductItemService productItemService;

    @Override
    public BigDecimal calCost(ProductItem productItem, String tenantId, Long time) {
        BigDecimal cost;
        ProductPrice productPrice = productPriceService.getAvailableProductPrice(productItem.getId());
        Double discount = productTenantDiscountService.getDiscountByProductIdAndTenantId(productItem.getId(), tenantId);
        if(productItem.getCalType() == Product.CAL_TYPE_NUM){
            //如果是计量，则只需单价*折扣
            cost = productPrice.getPrice().multiply(new BigDecimal(Double.toString(discount)));
        }else{
            //如果是计时，则只需 时长单位数量*单价*折扣
            Long calNum = this.calUnitNum(time, productPrice.getTimeUnit());
            cost = new BigDecimal(calNum).multiply(productPrice.getPrice()).multiply(new BigDecimal(Double.toString(discount))).setScale(4,BigDecimal.ROUND_HALF_UP);
        }
        return cost;
    }

    @Override
    public BigDecimal calCost(String code, String tenantId) {
        ProductItem productItem = productItemService.getProductItemByCode(code);
        ProductPrice productPrice = productPriceService.getAvailableProductPrice(productItem.getId());
        BigDecimal discount = new BigDecimal(productTenantDiscountService.getDiscountByProductIdAndTenantId(productItem.getId(), tenantId));
        return productPrice.getPrice().multiply(discount);
    }

    @Override
    public VoiceCdr callConsume(VoiceCdr cdr) {
        //因为就算时长是0秒也要算一个计费单位，所要加上1会更好处理一点
        Long time = cdr.getCallTimeLong() + 1;
        //扣费时长
        Long costTimeLong;
        //消费金额
        BigDecimal cost;
        //扣量
        Long deduct;
        //1.扣量2.扣费3.扣量加扣费
        Integer costType;
        if(time > 0){
            ProductCode productCode = ProductCode.valueOf(cdr.getType());
            String tenantId = cdr.getTenantId();
            String appId = cdr.getAppId();
            Date dt = cdr.getCallEndDt();
            ProductItem product = productItemService.getProductItemByCode(productCode.name());
            ProductPrice productPrice = productPriceService.getAvailableProductPrice(product.getId());
            //计量单位
            String unit = productPrice.getUnit();
            switch (productCode){
    //            case captcha_call:{
    //                //短信
    //                Long sms = calBillingService.getSms(tenantId);
    //                if(sms >0){
    //                    calBillingService.incUseSms(tenantId,dt,1L);
    //                    CaptchaUse captchaUse = new CaptchaUse(dt,productCode.name(),appId,tenantId);
    //                    captchaUseService.save(captchaUse);
    //                    costTimeLong = 0L;
    //                    cost = new BigDecimal(0);
    //                    deduct = 1L;
    //                    costType = VoiceCdr.COST_TYPE_DEDUCT;
    //                }else{
    //                    BigDecimal consume = insertConsume(tenantId, appId, time, dt, productCode.name(), productCode.getRemark(), product);
    //                    costTimeLong = 0L;
    //                    cost = consume;
    //                    deduct = 0L;
    //                    costType = VoiceCdr.COST_TYPE_COST;
    //                }
    //                break;
    //            }
                case sys_conf:{
                    //会议
                    Long useTime = calUnitNum(time, productPrice.getTimeUnit()) * productPrice.getTimeUnit();
                    Long conference = calBillingService.getConference(tenantId);
                    if(useTime <= conference){
                        calBillingService.incUseConference(tenantId,dt,useTime);
                        VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,useTime,productPrice.getTimeUnit(),productPrice.getUnit(),appId,tenantId);
                        voiceTimeUseService.save(use);
                        costTimeLong = useTime;
                        cost = new BigDecimal(0);
                        deduct = useTime;
                        costType = VoiceCdr.COST_TYPE_DEDUCT;
                    }else if(conference > 0){
                        //先扣量
                        calBillingService.incUseConference(tenantId,dt,conference);
                        VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,conference,productPrice.getTimeUnit(),productPrice.getUnit(),appId,tenantId);
                        voiceTimeUseService.save(use);
                        //再扣费
                        BigDecimal consume = insertConsume(tenantId, appId, useTime - conference, dt, productCode.name(), productCode.getRemark(), product);
                        costTimeLong = useTime;
                        cost = consume;
                        deduct = conference;
                        costType = VoiceCdr.COST_TYPE_COST_DEDUCT;
                    } else{
                        BigDecimal consume = insertConsume(tenantId, appId, time, dt, productCode.name(), productCode.getRemark(), product);
                        costTimeLong = useTime;
                        cost = consume;
                        deduct = 0L;
                        costType = VoiceCdr.COST_TYPE_COST;
                    }
                    break;
                }
                default:{
                    Long useTime = calUnitNum(time, productPrice.getTimeUnit()) * productPrice.getTimeUnit();
                    if(logger.isDebugEnabled()){
                        logger.info("扣费通话时长：{}",useTime);
                    }
                    //语音
                    Long voice = calBillingService.getVoice(tenantId);
                    if(logger.isDebugEnabled()){
                        logger.info("剩余语音时长 ：{}",voice);
                    }
                    if(useTime <= voice){
                        calBillingService.incUseVoice(tenantId,dt,useTime);
                        VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,useTime,productPrice.getTimeUnit(),productPrice.getUnit(),appId,tenantId);
                        voiceTimeUseService.save(use);
                        costTimeLong = useTime;
                        cost = new BigDecimal(0);
                        deduct = useTime;
                        costType = VoiceCdr.COST_TYPE_DEDUCT;
                    }else if(voice > 0){
                        //先扣量
                        calBillingService.incUseVoice(tenantId,dt,voice);
                        VoiceTimeUse use = new VoiceTimeUse(dt,productCode.name(),time,voice,productPrice.getTimeUnit(),productPrice.getUnit(),appId,tenantId);
                        voiceTimeUseService.save(use);
                        //再扣费
                        BigDecimal consume = insertConsume(tenantId, appId, useTime - voice, dt, productCode.name(), productCode.getRemark(), product);
                        costTimeLong = useTime;
                        cost = consume;
                        deduct = voice;
                        costType = VoiceCdr.COST_TYPE_COST_DEDUCT;
                    } else{
                        BigDecimal consume = insertConsume(tenantId, appId, time, dt, productCode.name(), productCode.getRemark(), product);
                        costTimeLong = useTime;
                        cost = consume;
                        deduct = 0L;
                        costType = VoiceCdr.COST_TYPE_COST;
                    }
                    break;
                }
            }
        }else{
            costTimeLong = 0L;
            cost = BigDecimal.ZERO;
            deduct = 0L;
            costType = VoiceCdr.COST_TYPE_COST;
        }
        cdr.setCostTimeLong(costTimeLong);
        cdr.setCost(cost);
        cdr.setDeduct(deduct);
        cdr.setCostType(costType);
        return cdr;
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
            case sys_conf:{
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
    private BigDecimal insertConsume(String tenantId, String appId, Long time, Date dt, String code, String remark,ProductItem product) {
        Tenant tenant = new Tenant();
        tenant.setId(tenantId);
        BigDecimal cost = this.calCost(product, tenantId, time);
        Consume consume = new Consume(dt,code,cost,remark,appId,tenant);
        consumeService.consume(consume);
        return cost;
    }

    /**
     * 多少个时长单位，时长/时长单位（不满一个时长单位按一个时长单位算)
     * @param time
     * @param timeUnit
     * @return
     */
    private Long calUnitNum(Long time, Integer timeUnit) {
        return new BigDecimal(time).divide(new BigDecimal(timeUnit),0,BigDecimal.ROUND_UP).longValue();
    }

    @Override
    public void recordConsume(VoiceFileRecord record){
        //因为就算时长是0秒也要算一个计费单位，所要加上1会更好处理一点
        Long time = record.getCallTimeLong() +1;
        ProductItem productItem = productItemService.getProductItemByCode(ProductCode.recording.name());
        ProductPrice productPrice = productPriceService.getAvailableProductPrice(productItem.getId());
        Long useTime = calUnitNum(time, productPrice.getTimeUnit()) * productPrice.getTimeUnit();
        record.setCostTimeLong(useTime);
        BigDecimal cost = insertConsume(record.getTenantId(), record.getAppId(), useTime, record.getCreateTime(), ProductCode.recording.name(), ProductCode.recording.getRemark(), productItem);
        record.setCost(cost);
    }
}
