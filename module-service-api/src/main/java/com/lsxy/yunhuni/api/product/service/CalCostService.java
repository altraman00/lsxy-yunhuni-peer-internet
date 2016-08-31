package com.lsxy.yunhuni.api.product.service;

import com.lsxy.yunhuni.api.product.model.Product;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by liups on 2016/8/27.
 */
public interface CalCostService {
    /**
     * 计算消费金额
     * @param product
     * @param tenantId
     * @param time
     * @return
     */
    BigDecimal calCost(Product product, String tenantId, Long time);

    /**
     * 消费计算金额及插入消费表，或插入扣量表
     * @param apiCmd    ProductCode枚举类型中的apiCmd，不对应就则取不到产品
     * @param tenantId
     * @param appId
     * @param time
     * @param dt    cdr中的结束时间
     */
    void consume(String apiCmd, String tenantId, String appId, Long time, Date dt);

}
