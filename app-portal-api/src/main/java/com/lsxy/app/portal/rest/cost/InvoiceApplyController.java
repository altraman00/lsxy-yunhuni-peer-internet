package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.consume.service.ConsumeMonthService;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@RequestMapping("/rest/invoice_apply")
@RestController
public class InvoiceApplyController extends AbstractRestController {
    @Autowired
    InvoiceApplyService invoiceApplyService;
    @Autowired
    TenantService tenantService;
    @Autowired
    ConsumeMonthService consumeMonthService;
    @Autowired
    InvoiceInfoService invoiceInfoService;

    @RequestMapping("/start_info")
    public RestResponse getStart(){
        Map<String,Object> result = new HashMap<>();
        Tenant tenant = tenantService.findTenantByUserName(this.getCurrentAccountUserName());
        //从申请历史中获取开始时间
        String start = invoiceApplyService.getStart(tenant.getId());
        if(StringUtils.isBlank(start)){
            //从开始消费的月统计中获取开始时间
            start = consumeMonthService.getStartMonthByTenantId(tenant.getId());
        }
        if(StringUtils.isBlank(start)){
            //用户没有任何消费
            result.put("amount",0);
        }else{
            BigDecimal amount = consumeMonthService.sumAmountByTime(tenant.getId(),start,null);
            result.put("start",start);
            result.put("amount",amount);
        }
        return RestResponse.success(result);
    }

    @RequestMapping("/page")
    public RestResponse page(Integer pageNo,Integer pageSize){
        Tenant tenant = tenantService.findTenantByUserName(this.getCurrentAccountUserName());
        Page<InvoiceApply> page = invoiceApplyService.getPage(tenant.getId(),pageNo,pageSize);
        return RestResponse.success(page);
    }

    @RequestMapping("/apply_amount")
    public RestResponse applyAmount(String start,String end){
        Tenant tenant = tenantService.findTenantByUserName(this.getCurrentAccountUserName());
        BigDecimal bigDecimal = consumeMonthService.sumAmountByTime(tenant.getId(), start, end);
        return RestResponse.success(bigDecimal);
    }

    @RequestMapping("/save")
    public RestResponse save(InvoiceApply apply){
        String userName = this.getCurrentAccountUserName();
        InvoiceApply result;
        //新建
        if(StringUtils.isBlank(apply.getId())){
            result = invoiceApplyService.create(apply,userName);
        }else{
            result = invoiceApplyService.update(apply, userName);
        }
        return RestResponse.success(result);
    }

    @RequestMapping("/get/{id}")
    public RestResponse get(@PathVariable String id){
        InvoiceApply a = invoiceApplyService.findById(id);
        return RestResponse.success(a);
    }



}
