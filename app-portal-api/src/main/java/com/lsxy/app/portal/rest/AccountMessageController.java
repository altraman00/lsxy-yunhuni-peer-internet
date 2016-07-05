package com.lsxy.app.portal.rest;

import com.lsxy.app.portal.base.AbstractRestController;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.web.rest.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zhangxb on 2016/7/5.
 */
@RequestMapping("/rest/message/account_message")
@PreAuthorize("hasAuthority('ROLE_TENANT_USER')")
@RestController
public class AccountMessageController extends AbstractRestController {
    @Autowired
    AccountMessageService accountMessageService;

    /**
     * 查询用户的消息
     * @param pageNo 第几页
     * @param pageSize 每页多少条记录
     * @return
     * @throws MatchMutiEntitiesException
     */
    @RequestMapping("/list")
    public RestResponse list(Integer pageNo,Integer pageSize) throws MatchMutiEntitiesException {
        String userName = getCurrentAccountUserName();
        Page<AccountMessage> list = accountMessageService.pageListByAccountId(userName,pageNo,pageSize);
        return RestResponse.success(list);
    }
}
