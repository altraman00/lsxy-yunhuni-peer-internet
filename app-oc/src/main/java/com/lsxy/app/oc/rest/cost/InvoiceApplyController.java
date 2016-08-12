package com.lsxy.app.oc.rest.cost;

import com.lsxy.app.oc.base.AbstractRestController;
import com.lsxy.framework.api.invoice.model.InvoiceApply;
import com.lsxy.framework.api.invoice.service.InvoiceApplyService;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.api.statistics.service.ConsumeMonthService;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.StringUtil;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 发票申请
 * Created by liups on 2016/7/21.
 */
@RequestMapping("/finance/invoice")
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
     * 发票申请分页获取
     * @param type await|auditing|unauth
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @param startTime 开始时间 yyyy-MM-dd
     * @param endTime 结束时间 yyyy-MM-dd
     * @return
     * @return
     */
    @RequestMapping("/{status}/list")
    public RestResponse page(@PathVariable String status, String name, String startTime, String endTime, Integer type, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        RestResponse restResponse = null;
        Page page = null;
        if("await".equals(status)||"auditing".equals(status)||"unauth".equals(status)) {
            Integer statusT = null;
            if("await".equals(status)){
                statusT = InvoiceApply.STATUS_SUBMIT;
            }else if("auditing".equals(status)){
                statusT = InvoiceApply.STATUS_DONE;
            }else if("unauth".equals(status)){
                statusT = InvoiceApply.STATUS_EXCEPTION;
            }
            if (StringUtil.isNotEmpty(name)) {
                List<Tenant> tList = tenantService.pageListByUserName(name);
                if (tList.size() == 0) {
                    page = null;
                } else {
                    String[] tenantId = new String[tList.size()];
                    for (int i = 0; i < tList.size(); i++) {
                        tenantId[i] = tList.get(i).getId();
                    }
                    page = invoiceApplyService.pageList(pageNo,pageSize,type,tenantId,statusT,startTime,endTime);
                }
            }else{
                page = invoiceApplyService.pageList(pageNo, pageSize, type, new String[]{},statusT, startTime, endTime);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }
        return RestResponse.success(page);
    }

    /**
     * 保存发票申请信息
     * @param id 发票记录id
     * @param status 修改状态 1通过2异常
     * @return
     */
    @RequestMapping("/edit/{id}")
    public RestResponse modify(@PathVariable String id,Integer status){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        RestResponse restResponse =null;
        if(status==InvoiceApply.STATUS_DONE||status==InvoiceApply.STATUS_EXCEPTION){
            invoiceApply.setStatus(status);
            invoiceApply = invoiceApplyService.save(invoiceApply);
        }else{
            restResponse = RestResponse.failed("0","参数错误");
        }
        if(restResponse==null){
            restResponse = RestResponse.success(invoiceApply);
        }
        return restResponse;
    }

    /**
     * 根据ID获取申请信息
     * @param id 发票记录
     * @return
     */
    @RequestMapping("/detail/{id}")
    public RestResponse get(@PathVariable String id){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        return RestResponse.success(invoiceApply);
    }



}
