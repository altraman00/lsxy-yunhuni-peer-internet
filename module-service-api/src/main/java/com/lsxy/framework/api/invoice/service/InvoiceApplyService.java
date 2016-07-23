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
     * @param pageNo 当前页
     * @param pageSize 每一页的数量
     * @return
     */
    Page<InvoiceApply> getPage(String tenantId, Integer pageNo, Integer pageSize);

    /**
     * 插入新的发票申请
     * @param apply
     * @param userName
     * @return
     */
    InvoiceApply create(InvoiceApply apply, String userName);

    /**
     * 修改更新（只有当状态为异常时才能调用此方法）
     * @param apply
     * @param userName
     * @return
     */
    InvoiceApply update(InvoiceApply apply, String userName);
}
