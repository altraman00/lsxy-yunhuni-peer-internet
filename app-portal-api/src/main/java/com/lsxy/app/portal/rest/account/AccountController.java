package com.lsxy.app.portal.rest.account;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.EntityUtils;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 用户管理
 * Created by Tandy on 2016/6/14.
 * Edit by zhangxb on 2016/07/15
 */
@RequestMapping("/rest/account")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class AccountController extends AbstractRestController {

    @Autowired
    private AccountService accountService;

    /**
     * 根据用户名获取用户对象
     * @return
     */
    @RequestMapping("/byun")
    public RestResponse findByUserName() throws MatchMutiEntitiesException {
        Account account = accountService.findAccountByUserName(getCurrentAccountUserName());
        return RestResponse.success(account);
    }

    /**
     * 更新数据
     * @param account
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/update")
    public RestResponse saveInformation(Account account) throws InvocationTargetException, IllegalAccessException {
        String userName = getCurrentAccountUserName();
        Account oldAccount = accountService.findAccountByUserName(userName);
        EntityUtils.copyProperties(oldAccount, account);
        oldAccount  = accountService.save(oldAccount);
        return RestResponse.success(oldAccount);
    }

}
