package com.lsxy.framework.message.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.message.model.AccountMessage;
import com.lsxy.framework.api.message.service.AccountMessageService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.message.dao.AccountMessageDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

/**
 * 用户消息实现类
 * Created by zhangxb on 2016/7/4.
 */
public class AccountMessageServiceImpl extends AbstractService<AccountMessage> implements AccountMessageService{
    @Autowired
    AccountMessageDao accountMessageDao;
    @Override
    public BaseDaoInterface<AccountMessage, Serializable> getDao() {
        return accountMessageDao;
    }
}
