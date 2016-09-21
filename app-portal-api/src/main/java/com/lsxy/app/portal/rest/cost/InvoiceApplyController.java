package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.yunhuni.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.EntityUtils;
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

    /**
     * 获取用户发票申请所能开始的时间，以及所能开发票的总金额
     * @return
     */
    @RequestMapping("/start_info")
    public RestResponse getStart(){
        String userName = this.getCurrentAccountUserName();
        Map<String,Object> result = new HashMap<>();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        //从申请历史中获取开始时间
        String start = invoiceApplyService.getStart(tenant.getId());
        if(StringUtils.isBlank(start)){
            //从开始消费的月统计中获取开始时间
            start = consumeMonthService.getStartMonthByTenantId(tenant.getId());
        }
        InvoiceInfo invoiceInfo = invoiceInfoService.getByUserName(userName);
        if(invoiceInfo != null){
            result.put("invoiceType",invoiceInfo.getType());
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

    /**
     * 发票申请分页获取
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/page")
    public RestResponse page(Integer pageNo,Integer pageSize){
        Tenant tenant = tenantService.findTenantByUserName(this.getCurrentAccountUserName());
        Page<InvoiceApply> page = invoiceApplyService.getPage(tenant.getId(),pageNo,pageSize,InvoiceApply.OPERATE_DONE);
        return RestResponse.success(page);
    }

    /**
     * 某一时段的发票金额
     * @param start
     * @param end
     * @return
     */
    @RequestMapping("/apply_amount")
    public RestResponse applyAmount(String start,String end){
        Tenant tenant = tenantService.findTenantByUserName(this.getCurrentAccountUserName());
        BigDecimal bigDecimal = consumeMonthService.sumAmountByTime(tenant.getId(), start, end);
        return RestResponse.success(bigDecimal);
    }

    /**
     * 保存发票申请信息
     * @param apply
     * @return
     */
    @RequestMapping("/save")
    public RestResponse save(InvoiceApply apply){
        String userName = this.getCurrentAccountUserName();
        Tenant tenant = tenantService.findTenantByUserName(userName);
        InvoiceApply result;
        //新建
        if(StringUtils.isBlank(apply.getId())){
            result = invoiceApplyService.create(apply,userName);
        }else{
            //修改
            InvoiceApply oldApply = invoiceApplyService.findById(apply.getId());
            if(oldApply.getStatus() != InvoiceApply.STATUS_EXCEPTION){
                throw new RuntimeException("只有异常的发票申请才能修改");
            }
            if(!apply.getStart().equals(oldApply.getStart()) || !apply.getEnd().equals(oldApply.getEnd()) || !apply.getType().equals(oldApply.getType())||
                    !oldApply.getTenant().getId().equals(tenant.getId())){
                throw new RuntimeException("数据异常");
            }
            try {
                //异常处理操作完成
                oldApply.setOperate(InvoiceApply.OPERATE_DONE);
                result = invoiceApplyService.save(oldApply);
                //生成新纪录
                EntityUtils.copyProperties(oldApply,apply);
                oldApply.setStatus(InvoiceApply.STATUS_SUBMIT);
                oldApply.setApplyTime(new Date());
                oldApply.setRemark(null);
                oldApply.setId(null);
                oldApply.setOperate(null);
                result = invoiceApplyService.save(oldApply);
            } catch (Exception e) {
                throw new RuntimeException("数据异常",e);
            }
        }
        return RestResponse.success(result);
    }

    /**
     * 根据ID获取申请信息
     * @param id
     * @return
     */
    @RequestMapping("/get/{id}")
    public RestResponse get(@PathVariable String id){
        InvoiceApply a = invoiceApplyService.findById(id);
        return RestResponse.success(a);
    }



}
