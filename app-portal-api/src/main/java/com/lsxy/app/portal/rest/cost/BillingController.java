package com.lsxy.app.portal.rest.cost;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.web.rest.RestResponse;
import com.lsxy.framework.api.billing.service.CalBillingService;
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
//    @Autowired
//    private BillingService billingService;
    @Autowired
    private CalBillingService calBillingService;
    /**
     * 查找当前用户所属租户的账务
     * @throws Exception
     */
    @RequestMapping("/get")
    public RestResponse getBilling() throws Exception{
        Account account = this.getCurrentAccount();
        Billing billing = calBillingService.getCalBilling(account.getTenant().getId());
//        Billing billing = billingService.findBillingByUserName(getCurrentAccountUserName());
        return RestResponse.success(billing);
    }
}
