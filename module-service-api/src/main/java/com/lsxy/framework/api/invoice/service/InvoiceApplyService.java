package com.lsxy.framework.api.invoice.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.core.utils.Page;

/**
 * 发票申请Service
 * Created by liups on 2016/7/21.
 */
public interface InvoiceApplyService extends BaseService<InvoiceApply> {
    /**
     * 获取能开发票的开始时间
     * @return
     */
    String getStart(String tenantId);

    /**
     * 获取发票申请记录
     * @param tenantId
     * @param pageNo
     * @param pageSize
     * @return
     */
    Page<InvoiceApply> getPage(String tenantId, Integer pageNo, Integer pageSize);
}
