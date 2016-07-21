package com.lsxy.framework.invoice.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.invoice.dao.InvoiceApplyDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liups on 2016/7/21.
 */
@Service
public class InvoiceApplyServiceImpl extends AbstractService<InvoiceApply> implements InvoiceApplyService {

    @Autowired
    InvoiceApplyDao invoiceApplyDao;

    @Override
    public BaseDaoInterface<InvoiceApply, Serializable> getDao() {
        return this.invoiceApplyDao;
    }

    @Override
    public String getStart(String tenantId) {
        InvoiceApply invoiceApply = invoiceApplyDao.findFirst1ByTenantIdOrderByEndDesc(tenantId);
        if(invoiceApply != null){
            String end = new SimpleDateFormat("yyyy-MM").format(invoiceApply.getEnd());
            return DateUtils.getNextMonth(end, "yyyy-MM");
        }else{
            return null;
        }
    }

    @Override
    public Page<InvoiceApply> getPage(String tenantId, Integer pageNo, Integer pageSize) {
        String hql = "from InvoiceApply obj where obj.tenant.id = ?1 order by obj.createTime";
        return this.pageList(hql, pageNo, pageSize, tenantId);
    }

}
