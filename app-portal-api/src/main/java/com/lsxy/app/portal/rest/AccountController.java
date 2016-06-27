package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.tenant.dao.AccountDao;
import com.lsxy.framework.tenant.model.Account;
import com.lsxy.framework.tenant.service.AccountService;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tandy on 2016/6/14.
 */
@RequestMapping("/rest/account")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class AccountController extends AbstractRestController {

    @Autowired
    private AccountService accountService;

    @RequestMapping("/")
    public RestResponse restMyAccountInfo(){
        Account account = new Account();
        account.setId("1234123412341234");
        return RestResponse.success(account);
    }


    @RequestMapping("/test001")
    public RestResponse test001(){
        Account  account = accountService.findById("1");
        return RestResponse.success(account);
    }

}
