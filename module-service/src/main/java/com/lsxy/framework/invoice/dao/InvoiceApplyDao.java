package com.lsxy.framework.invoice.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.invoice.model.InvoiceApply;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/21.
 */
public interface InvoiceApplyDao extends BaseDaoInterface<InvoiceApply, Serializable> {
    /**
     * 获取最新一条发票申请的记录
     * @param tenantId
     * @return
     */
    InvoiceApply findFirst1ByTenantIdOrderByEndDesc(String tenantId);
}
