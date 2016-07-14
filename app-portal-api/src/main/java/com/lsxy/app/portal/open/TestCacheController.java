package com.lsxy.app.portal.open;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.web.rest.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Tandy on 2016/7/13.
 */
@RestController
@Profile({"local","development","test"})
@RequestMapping("/open/test/cache")
public class TestCacheController {

    public static final Logger logger = LoggerFactory.getLogger(TestCacheController.class);
    @Autowired
    private AccountService accountService;

    @RequestMapping("/account/{id}")
    public RestResponse<Account> testCacheAccount(@PathVariable String id){
        Account account = accountService.findById(id);
        return RestResponse.success(account);
    }

    @RequestMapping("/account/save/{id}")
    public RestResponse testSaveCache(@PathVariable String id,String userName){
        Account account = accountService.findById(id);
        account.setUserName(userName);
        accountService.save(account);
        if (logger.isDebugEnabled()){
                logger.debug("修改用户名为：{}",userName);
         }
        return RestResponse.success("OK");
    }

    @RequestMapping("/account/del/{id}")
    public RestResponse testDelCache(@PathVariable String id) throws InvocationTargetException, IllegalAccessException {
        accountService.delete(id);
        if (logger.isDebugEnabled()){
            logger.debug("删除：{}",id);
        }
        return RestResponse.success("OK");
    }

}
