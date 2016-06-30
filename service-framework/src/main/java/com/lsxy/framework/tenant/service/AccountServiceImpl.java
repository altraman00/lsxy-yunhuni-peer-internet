package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.tenant.dao.AccountDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/6/24.
 */
@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {
    private static final Logger logger = LoggerFactory.getLogger(RealnameRocpServiceImpl.class);
    @Autowired
    private AccountDao accountDao;

    @Override
    public BaseDaoInterface<Account, Serializable> getDao() {
        return accountDao;
    }


    @Override
    public Account findPersonByLoginNameAndPassword(String userLoginName, String password) throws MatchMutiEntitiesException {
        String hql = "from Account obj where obj.userName=?1 or obj.email=?2 or obj.mobile=?3";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName);
        if(account != null) {
            if(!account.getPassword().equals(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()))){
                 account = null;
            }
        }
        return account;
    }

    @Override
    public Account findByUserName(String userName) {
        try {
            Account account = accountDao.findByUserName(userName);
            return account;
        }catch(Exception e){
            logger.error("AccountServiceImpl.findByUserName:{}",e);
            return null;
        }
    }


}
