package com.lsxy.framework.invoice.dao;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.tenant.model.Tenant;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/14.
 */
public interface InvoiceInfoDao extends BaseDaoInterface<InvoiceInfo, Serializable> {
    InvoiceInfo findByTenant(Tenant tenant);
}
