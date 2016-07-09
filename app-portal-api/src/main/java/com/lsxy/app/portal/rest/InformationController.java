package com.lsxy.app.portal.rest;

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

/**
 * Created by zhangxb on 2016/6/28.
 * 基础资料
 */
@RequestMapping("/rest/account/information")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class InformationController extends AbstractRestController {

    @Autowired
    private AccountService accountService;

    /**
     * 根据用户名获取用户对象
     * Account 用户类
     * return
     */
    @RequestMapping("/save_information")
    public RestResponse saveInformation(Account account) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        Account oldAccount = accountService.findAccountByUserName(userName);
        try {
            EntityUtils.copyProperties(oldAccount, account);
        } catch (Exception e) {
            return RestResponse.failed("4001","转换类失败");
        }
        oldAccount  = accountService.save(oldAccount);
        if(oldAccount == null){
            return RestResponse.failed("0020","更新数据库失败");
        }
        return RestResponse.success(oldAccount);
    }

}
