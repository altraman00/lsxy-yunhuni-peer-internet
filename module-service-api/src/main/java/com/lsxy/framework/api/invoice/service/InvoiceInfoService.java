package com.lsxy.framework.api.invoice.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;

/**
 * 发票信息
 * Created by liups on 2016/7/14.
 */
public interface InvoiceInfoService extends BaseService<InvoiceInfo> {
    InvoiceInfo getByUserName(String userName);
}
