package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.Iterator;
import java.util.List;

import static javafx.scene.input.KeyCode.T;
import static org.apache.zookeeper.Environment.list;

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

    /**
     *
     * @return
     */
    @RequestMapping("/test002")
    public RestResponse test002(){
        Page<Account> xx = accountService.pageList(1,10);
        List<Account> result = xx.getResult();
        return RestResponse.success(result);
    }

}
