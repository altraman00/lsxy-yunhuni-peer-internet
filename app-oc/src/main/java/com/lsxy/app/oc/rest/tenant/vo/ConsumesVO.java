package com.lsxy.app.oc.rest.tenant.vo;

import com.lsxy.yunhuni.api.consume.model.Consume;
import com.lsxy.framework.core.utils.Page;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by liuws on 2016/8/12.
 */
public class ConsumesVO implements Serializable{

    private Page<Consume> consumes;

    private BigDecimal sumAmount;

    public Page<Consume> getConsumes() {
        return consumes;
    }

    public void setConsumes(Page<Consume> consumes) {
        this.consumes = consumes;
    }

    public BigDecimal getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(BigDecimal sumAmount) {
        this.sumAmount = sumAmount;
    }
}
