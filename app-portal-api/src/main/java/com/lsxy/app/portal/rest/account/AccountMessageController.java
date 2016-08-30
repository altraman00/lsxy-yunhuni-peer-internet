package com.lsxy.app.portal.rest.account;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;

/**
 * 用户消息
 * Created by zhangxb on 2016/7/5.
 */
@RequestMapping("/rest/message/account_message")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class AccountMessageController extends AbstractRestController {
    @Autowired
    AccountMessageService accountMessageService;

    /**
     * 查询用户消息
     * @param pageNo 第几页
     * @param pageSize 每页多少条记录
     * @return
     */
    @RequestMapping("/list")
    public RestResponse list(Integer pageNo,Integer pageSize){
        String userName = getCurrentAccountUserName();
        Page<AccountMessage> list = accountMessageService.pageListByAccountId(userName,pageNo,pageSize);
        return RestResponse.success(list);
    }
    /**
     * 修改状态为已读
     * @return
     */
    @RequestMapping("/edit")
    public RestResponse modifyStauts(){
        Account account = getCurrentAccount();
        accountMessageService.modifyMessageStatus(account.getId(),AccountMessage.READ);
        return RestResponse.success();
    }
    /**
     * 根据状态查询数量
     * @param status
     * @return
     */
    @RequestMapping("/count")
    public RestResponse count(Integer status){
        String userName = getCurrentAccountUserName();
        Long num = accountMessageService.count(userName,status);
        return RestResponse.success(num);
    }
    /**
     * 根据用户消息id删除用户消息记录
     * @param id 用户消息id
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    @RequestMapping("/delete")
    public RestResponse delete(String id) throws InvocationTargetException, IllegalAccessException {
        AccountMessage accountMessage = accountMessageService.findById(id);
        accountMessageService.delete(accountMessage);
        return RestResponse.success(accountMessage);
    }
}
