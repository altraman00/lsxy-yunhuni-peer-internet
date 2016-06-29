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
 * 账务RestApi接口
 * Created by liups on 2016/6/28.
 */
@RequestMapping("/rest/billing")
@RestController
public class BillingController extends AbstractRestController {
    @Autowired
    private BillingService billingService;

    /**
     * 查找当前用户所属租户的账务
     * @throws Exception
     */
    @RequestMapping("/get")
    public RestResponse getBilling() throws Exception{
        Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
        return RestResponse.success(billing);
    }
}
