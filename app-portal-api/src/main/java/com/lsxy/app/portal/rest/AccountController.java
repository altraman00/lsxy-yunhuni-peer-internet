package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.tenant.model.Account;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * Created by Tandy on 2016/6/14.
 */
@RequestMapping("/rest/account")
@RestController
public class AccountController extends AbstractRestController {

    @RequestMapping("/")
    public Account restMyAccountInfo(){
        return new Account();
    }

}
