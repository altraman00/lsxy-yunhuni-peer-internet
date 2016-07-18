package com.lsxy.framework.invoice.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.invoice.dao.InvoiceInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by liups on 2016/7/14.
 */
@Service
public class InvoiceInfoServiceImpl extends AbstractService<InvoiceInfo> implements InvoiceInfoService {
    @Autowired
    InvoiceInfoDao invoiceInfoDao;

    @Autowired
    TenantService tenantService;

    @Override
    public BaseDaoInterface<InvoiceInfo, Serializable> getDao() {
        return this.invoiceInfoDao;
    }

    @Override
    public InvoiceInfo getByUserName(String userName) {
        InvoiceInfo invoiceInfo = null;
        Tenant tenant = tenantService.findTenantByUserName(userName);
        if(tenant != null){
            invoiceInfo = invoiceInfoDao.findByTenant(tenant);
        }
        return invoiceInfo;
    }

    @Override
    public InvoiceInfo create(InvoiceInfo invoiceInfo, String userName) {
        Tenant tenant = tenantService.findTenantByUserName(userName);
        invoiceInfo.setTenant(tenant);
        this.save(invoiceInfo);
        return invoiceInfo;
    }


}
