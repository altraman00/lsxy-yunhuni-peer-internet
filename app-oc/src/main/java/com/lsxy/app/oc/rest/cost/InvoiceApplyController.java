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
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * @param status auditing|unauth
     * @param pageNo 第几页
     * @param pageSize 每页记录数
     * @param name 名字
     * @return
     * @return
     */
    @RequestMapping(value = "/{status}/send/list",method = RequestMethod.GET)
    public RestResponse page(@ApiParam(name = "status",value = "auditing|unauth") @PathVariable String status,@RequestParam(required=false)Integer type,
                             @RequestParam(required=false)String name, @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
        RestResponse restResponse = null;
        Page page = null;
        if("auditing".equals(status)||"unauth".equals(status)) {
            String statusT = null;
            if("auditing".equals(status)){
                statusT = "";
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
                    page = invoiceApplyService.pageList(pageNo,pageSize,tenantId,statusT,type);
                }
            }else{
                page = invoiceApplyService.pageList(pageNo, pageSize, new String[]{},statusT,type);
            }
            restResponse = RestResponse.success(page);
        }else{
            restResponse = RestResponse.failed("0","访问路劲不存在");
        }
        return RestResponse.success(page);
    }
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
    @RequestMapping(value = "/{status}/list",method = RequestMethod.GET)
    public RestResponse page(@ApiParam(name = "status",value = "await|auditing|unauth") @PathVariable String status,
                             @RequestParam(required=false)String name, @RequestParam(required=false)String startTime,
                             @RequestParam(required=false) String endTime, @RequestParam(required=false)Integer type,
                             @RequestParam(defaultValue = "1")Integer pageNo, @RequestParam(defaultValue = "20") Integer pageSize){
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
     * @param invoiceApplyVo 接收参数对象
     * @return
     */
    @RequestMapping(value = "/edit",method = RequestMethod.POST)
    public RestResponse modify(@RequestBody  InvoiceApplyVo invoiceApplyVo){
        String id = invoiceApplyVo.getId();
        Integer status = invoiceApplyVo.getStatus();
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
     * 保存发票申请信息
     * @param invoiceApplyVo 接收参数对象
     * @return
     */
    @RequestMapping(value = "/edit/send",method = RequestMethod.POST)
    public RestResponse modifySend(@RequestBody InvoiceApplyVo invoiceApplyVo){
        InvoiceApply invoiceApply = invoiceApplyService.findById(invoiceApplyVo.getId());
        RestResponse restResponse =null;
        if(invoiceApply.getStatus()==InvoiceApply.STATUS_DONE){
            invoiceApply.setExpressNo(invoiceApplyVo.getExpressNo());
            invoiceApply.setExpressCom(invoiceApplyVo.getExpressCom());
            invoiceApply = invoiceApplyService.save(invoiceApply);
        }else{
            restResponse = RestResponse.failed("0","该发票审核未通过，不能发送");
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
    @RequestMapping(value = "/detail/{id}",method = RequestMethod.GET)
    public RestResponse get(@PathVariable String id){
        InvoiceApply invoiceApply = invoiceApplyService.findById(id);
        return RestResponse.success(invoiceApply);
    }



}
