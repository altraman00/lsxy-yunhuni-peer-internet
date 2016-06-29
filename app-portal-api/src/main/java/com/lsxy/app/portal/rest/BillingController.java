package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.yuhuni.billing.model.Billing;
import com.lsxy.yuhuni.billing.service.BillingService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * Created by liups on 2016/6/28.
 */
@RequestMapping("/rest/billing")
@RestController
public class BillingController extends AbstractRestController {
    @Autowired
    private BillingService billingService;

    @RequestMapping("/get")
    public RestResponse getBilling() throws Exception{
        Account currentAccount = getCurrentAccount();
        Billing billing = billingService.findBillingByUserName(currentAccount.getUserName());
        return RestResponse.success(billing);
    }
}
