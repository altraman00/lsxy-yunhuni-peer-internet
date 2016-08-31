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
     * 消费
     * @param apiCmd
     * @param tenantId
     * @param appId
     * @param time
     * @param dt
     */
    void consume(String apiCmd, String tenantId, String appId, Long time, Date dt);

}
