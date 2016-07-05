package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.message.dao.AccountMessageDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import scala.App;

import java.io.Serializable;
import java.util.List;

/**
 * 用户消息实现类
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class AccountMessageServiceImpl extends AbstractService<AccountMessage> implements AccountMessageService{
    @Autowired
    AccountMessageDao accountMessageDao;
    @Autowired
    AccountService accountService;
    @Override
    public BaseDaoInterface<AccountMessage, Serializable> getDao() {
        return accountMessageDao;
    }

    @Override
    public Page<AccountMessage> pageListByAccountId(String userName,Integer pageNo,Integer pageSize) throws MatchMutiEntitiesException {
        Account account = accountService.findAccountByUserName(userName);
        String hql = "from AccountMessage obj where obj.account.id="+account.getId()+" and `status`<>'-1' order by status asc,create_time desc";
        Page<AccountMessage> page =  this.pageList(hql,pageNo,pageSize);
        return page;
    }
}
