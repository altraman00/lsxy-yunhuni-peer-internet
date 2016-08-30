package com.lsxy.yunhuni.api.product.service;

import java.math.BigDecimal;

/**
 * Created by liups on 2016/8/27.
 */
public interface CalCostService {
    BigDecimal calCost(String code, String tenantId, Long time);
}
