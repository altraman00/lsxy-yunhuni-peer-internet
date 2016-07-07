package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.tenant.dao.AccountDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by Tandy on 2016/6/24.
 */
@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {

    @Autowired
    private AccountDao accountDao;
    @Autowired
    private TenantService tenantService;

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
    public Account findAccountByLoginName(String userLoginName) throws MatchMutiEntitiesException {
        String hql = "from Account obj where obj.userName=?1 or obj.email=?2 or obj.mobile=?3";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName);
        return account;
    }

    @Override
    public Account findAccountByUserName(String userName) throws MatchMutiEntitiesException {
        return accountDao.findByUserName(userName);
    }

    @Override
    public int checkRegInfo(String userName, String mobile, String email){
        Long userNameCount = accountDao.countByUserNameAndStatus(userName,Account.STATUS_NORMAL);
        if(userNameCount > 0){
            return AccountService.REG_CHECK_USERNAME_EXIST;
        }
        Long mobileCount = accountDao.countByMobileAndStatus(mobile,Account.STATUS_NORMAL);
        if(mobileCount > 0){
            return AccountService.REG_CHECK_MOBILE_EXIST;
        }
        Long emailCount = accountDao.countByEmailAndStatus(email,Account.STATUS_NORMAL);
        if(emailCount > 0){
            return AccountService.REG_CHECK_MOBILE_EXIST;
        }
        return AccountService.REG_CHECK_PASS;
    }


    @Override
    public Account createAccount(String userName, String mobile, String email) {
        Tenant tenant = tenantService.createTenant();
        Account account = new Account();
        account.setUserName(userName);
        account.setMobile(mobile);
        account.setEmail(email);
        account.setStatus(Account.STATUS_NOT_ACTIVE);
        account.setTenant(tenant);
        return this.save(account);
    }

    @Override
    public Account activeAccount(String accountId, String password) {
        Account account = this.findById(accountId);
        //账号是否是未激活状态
        if(account != null && Account.STATUS_NOT_ACTIVE == account.getStatus()){
            //校验激活信息是否重复
            boolean flag = this.checkActiveInfo(account.getUserName(),account.getMobile(),account.getEmail());
            if(flag){
                //修改激活状态
                account.setStatus(Account.STATUS_NORMAL);
                this.save(account);
                //创建主鉴权账号
                Tenant tenant = account.getTenant();

                //TODO 帐务数据创建

            }else{
            }
        }else if( Account.STATUS_NORMAL == account.getStatus()){
        }
        return null;
    }

    /**
     * 检查激活信息是否可用，各个信息在数据库中是否有重复
     * @param userName 用户名
     * @param mobile 手机号
     * @param email 邮箱
     * @return
     */
    public boolean checkActiveInfo(String userName, String mobile, String email){
        Long userNameCount = accountDao.countByUserNameAndStatus(userName,Account.STATUS_NORMAL);
        if(userNameCount > 0){
            return false;
        }
        Long mobileCount = accountDao.countByMobileAndStatus(mobile,Account.STATUS_NORMAL);
        if(mobileCount > 0){
            return false;
        }
        Long emailCount = accountDao.countByEmailAndStatus(email,Account.STATUS_NORMAL);
        if(emailCount > 0){
            return false;
        }
        return true;
    }

}
