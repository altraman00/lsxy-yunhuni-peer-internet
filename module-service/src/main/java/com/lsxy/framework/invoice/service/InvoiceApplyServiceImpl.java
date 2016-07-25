package com.lsxy.framework.invoice.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.consume.service.ConsumeMonthService;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.invoice.dao.InvoiceApplyDao;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liups on 2016/7/21.
 */
@Service
public class InvoiceApplyServiceImpl extends AbstractService<InvoiceApply> implements InvoiceApplyService {

    @Autowired
    InvoiceApplyDao invoiceApplyDao;
    @Autowired
    InvoiceInfoService invoiceInfoService;
    @Autowired
    TenantService tenantService;
    @Autowired
    ConsumeMonthService consumeMonthService;

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

    @Override
    public InvoiceApply create(InvoiceApply apply, String userName) {
        InvoiceInfo info = invoiceInfoService.getByUserName(userName);
        if(!info.getType().equals(apply.getType())){
            throw new RuntimeException("数据异常");
        }
        Tenant tenant = tenantService.findTenantByUserName(userName);
        String start = DateUtils.getDate(apply.getStart(),"yyyy-MM");
        String end = DateUtils.getDate(apply.getEnd(), "yyyy-MM");
        //从申请历史中获取开始时间
        String applyStart = this.getStart(tenant.getId());
        if(StringUtils.isBlank(applyStart)){
            //从开始消费的月统计中获取开始时间
            applyStart = consumeMonthService.getStartMonthByTenantId(tenant.getId());
        }
        Date limitEnd = DateUtils.parseDate(DateUtils.getDate(new Date(), "yyyy-MM"),"yyyy-MM");
        //检验时间有效性
        if(StringUtils.isBlank(applyStart)||!applyStart.equals(start) || apply.getEnd().getTime() >= limitEnd.getTime()){
            throw new RuntimeException("数据异常");
        }
        BigDecimal amount = consumeMonthService.sumAmountByTime(tenant.getId(), start,end);
        //个人增值税普通发票(100元起)，企业增值税普通发票(100元起)
        if((apply.getType() == 1 || apply.getType() == 2) && amount.compareTo(new BigDecimal(100)) == -1){
            throw new RuntimeException("个人增值税普通发票(100元起)，企业增值税普通发票(100元起),金额太小，不足以开发票");
        }else if(apply.getType() == 3 && amount.compareTo(new BigDecimal(1000)) == -1){
            //企业增值税专用发票(1000元起)
            throw new RuntimeException("企业增值税专用发票(1000元起),金额太小，不足以开发票");
        }
        apply.setAmount(amount);
        apply.setTenant(tenant);
        apply.setStatus(InvoiceApply.STATUS_SUBMIT);
        apply.setApplyTime(new Date());
        this.save(apply);
        return apply;
    }

    @Override
    public InvoiceApply update(InvoiceApply apply, String userName) {
        InvoiceApply oldApply = this.findById(apply.getId());
        if(oldApply.getStatus() != InvoiceApply.STATUS_EXCEPTION){
            throw new RuntimeException("只有异常的发票申请才能修改");
        }
        if(!apply.getStart().equals(oldApply.getStart()) || !apply.getEnd().equals(oldApply.getEnd()) || !apply.getType().equals(oldApply.getType())){
            throw new RuntimeException("数据异常");
        }
        try {
            EntityUtils.copyProperties(oldApply,apply);
            oldApply.setStatus(InvoiceApply.STATUS_SUBMIT);
            oldApply.setApplyTime(new Date());
            oldApply.setRemark(null);
            this.save(oldApply);
        } catch (Exception e) {
            throw new RuntimeException("数据异常",e);
        }
        return oldApply;
    }


}
