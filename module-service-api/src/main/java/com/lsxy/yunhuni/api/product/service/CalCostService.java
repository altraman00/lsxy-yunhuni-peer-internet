package com.lsxy.yunhuni.api.product.service;

import com.lsxy.yunhuni.api.file.model.VoiceFileRecord;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.model.ProductItem;
import com.lsxy.yunhuni.api.session.model.VoiceCdr;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/27.
 */
public interface CalCostService {
    /**
     * 计算消费金额
     * @param productItem
     * @param tenantId
     * @param time
     * @return
     */
    BigDecimal calCost(ProductItem productItem, String tenantId, Long time);
    BigDecimal calCost(String code, String tenantId);
    /**
     * 消费计算金额及插入消费表，或插入扣量表
     * @param cdr cdr数据 传入的cdr数据将会被继续完善相关消费信息
     */
    VoiceCdr callConsumeCal(VoiceCdr cdr);

    /**
     * 判断是否有剩余呼叫时间或者余额是否充足
     * @param apiCmd
     * @param tenantId
     * @return
     */
    boolean isCallTimeRemainOrBalanceEnough(String apiCmd, String tenantId);

    void recordConsumeCal(VoiceFileRecord record);
}
