package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.invoice.model.InvoiceInfo;
import com.lsxy.framework.api.invoice.service.InvoiceInfoService;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 发票信息
 * Created by liups on 2016/7/14.
 */
@RequestMapping("/rest/invoice_info")
@RestController
public class InvoiceInfoController extends AbstractRestController {

    @Autowired
    InvoiceInfoService invoiceInfoService;

    @RequestMapping("/get")
    public RestResponse<InvoiceInfo> get(){
        String userName = this.getCurrentAccountUserName();
        InvoiceInfo invoiceInfo = invoiceInfoService.getByUserName(userName);
        return RestResponse.success(invoiceInfo);
    }

    @RequestMapping("/save")
    public RestResponse<InvoiceInfo> save(InvoiceInfo invoiceInfo) throws InvocationTargetException, IllegalAccessException {
        String userName = this.getCurrentAccountUserName();
        InvoiceInfo dbInvoiceInfo = invoiceInfoService.getByUserName(userName);
        if(dbInvoiceInfo != null){
            EntityUtils.copyProperties(dbInvoiceInfo,invoiceInfo);
            invoiceInfoService.save(dbInvoiceInfo);
            invoiceInfo = dbInvoiceInfo;
        }else{
            invoiceInfoService.create(invoiceInfo,userName);
        }
        return RestResponse.success(invoiceInfo);
    }

}
