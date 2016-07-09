package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.message.dao.AccountMessageDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import java.io.Serializable;

/**
 * 用户消息实现类
 * Created by zhangxb on 2016/7/4.
 */
@Service
public class AccountMessageServiceImpl extends AbstractService<AccountMessage> implements AccountMessageService{
    private static final Logger logger = LoggerFactory.getLogger(AccountMessageServiceImpl.class);
    @Autowired
    AccountMessageDao accountMessageDao;
    @Autowired
    AccountService accountService;
    @Override
    public BaseDaoInterface<AccountMessage, Serializable> getDao() {
        return accountMessageDao;
    }

    @Override
    public Page<AccountMessage> pageListByAccountId(String userName,Integer pageNo,Integer pageSize) {
        Account  account = accountService.findAccountByUserName(userName);
        String hql = "from AccountMessage obj where obj.account.id="+account.getId()+" and obj.status <>-1 order by status asc,create_time desc";
        Page<AccountMessage> page =  this.pageList(hql,pageNo,pageSize);
        return page;
    }

    @Override
    public Long count(String userName, Integer status) {
        Long result = 0l;
        Account  account = accountService.findAccountByUserName(userName);
        String hql = "select count(1) from AccountMessage obj where deleted=0 and obj.account.id=?1 and obj.status=?2 ";
        Query query = this.getEm().createQuery(hql);
        query.setParameter(1,account.getId());
        query.setParameter(2,status);
        Object obj = query.getSingleResult();
        if(obj instanceof Long){
            result = (Long) obj;
        }
        return result;
    }
}
