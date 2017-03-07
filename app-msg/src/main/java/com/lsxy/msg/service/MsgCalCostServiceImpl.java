package com.lsxy.msg.service;

import com.lsxy.msg.api.model.MsgSendRecord;
import com.lsxy.msg.api.service.MsgCalCostService;
import com.lsxy.yunhuni.api.product.enums.ProductCode;
import com.lsxy.yunhuni.api.product.service.CalCostService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

/**
 * Created by liups on 2017/3/6.
 */
public class MsgCalCostServiceImpl implements MsgCalCostService {
    @Autowired
    CalCostService calCostService;

    @Override
    public void calMsgCost(MsgSendRecord record) {
        BigDecimal unitCost = calCostService.calCost(ProductCode.msg_sms.name(), record.getTenantId());
        BigDecimal cost = unitCost.multiply(new BigDecimal(record.getSumNum()));
        record.setMsgCost(cost);
    }
}
