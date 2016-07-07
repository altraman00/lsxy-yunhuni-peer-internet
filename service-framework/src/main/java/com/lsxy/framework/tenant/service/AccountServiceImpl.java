package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.exceptions.RegisterException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.tenant.dao.AccountDao;
import org.apache.commons.lang.StringUtils;
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
        String hql = "from Account obj where obj.userName=?1 or obj.email=?2 or obj.mobile=?3 and obj.status=?4";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName,Account.STATUS_NORMAL);
        if(account != null) {
            if(!account.getPassword().equals(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()))){
                 account = null;
            }
        }
        return account;
    }

    @Override
    public Account findAccountByLoginName(String userLoginName) throws MatchMutiEntitiesException {
        String hql = "from Account obj where obj.userName=?1 or obj.email=?2 or obj.mobile=?3 and obj.status=?4";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName,Account.STATUS_NORMAL);
        return account;
    }

    @Override
    public Account findAccountByUserName(String userName) throws MatchMutiEntitiesException {
        return accountDao.findByUserNameAndStatus(userName,Account.STATUS_NORMAL);
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
    public Account activeAccount(String accountId, String password) throws RegisterException{
        Account account = this.findById(accountId);
        //账号是否是未激活状态
        if(account != null && Account.STATUS_NOT_ACTIVE == account.getStatus()&& StringUtils.isNotBlank(password)){
            //校验激活信息是否重复
            boolean flag = this.checkActiveInfo(account.getUserName(),account.getMobile(),account.getEmail());
            if(flag){
                //修改激活状态,设置密码
                account.setStatus(Account.STATUS_NORMAL);
                account.setPassword(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()));
                this.save(account);
                Tenant tenant = account.getTenant();
                //TODO 创建主鉴权账号

                //TODO 帐务数据创建

                //TODO 默认将用户手机号作为当前租户的测试号码

            }else{
                throw new RegisterException("注册信息不可用，已存在重复的注册信息！");
            }
        }else if( Account.STATUS_NORMAL == account.getStatus()){
            throw new RegisterException("账号已激活，请勿重复操作");
        }else if(Account.STATUS_EXPIRE == account.getStatus()){
            throw new RegisterException("账号已过期");
        }else{
            throw new RegisterException("账号激活失败");
        }
        return account;
    }

    @Override
    public boolean checkEmail(String email) {
        Long count = accountDao.countByEmailAndStatus(email, Account.STATUS_NORMAL);
        return count > 0;
    }

    @Override
    public boolean checkMobile(String mobile) {
        Long count = accountDao.countByMobileAndStatus(mobile, Account.STATUS_NORMAL);
        return count > 0;
    }

    @Override
    public void resetPwdByEmail(String email, String password) {
        Account account = accountDao.findByEmailAndStatus(email, Account.STATUS_NORMAL);
        account.setPassword(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()));
        this.save(account);
    }

    @Override
    public void resetPwdByMobile(String mobile, String password) {
        Account account = accountDao.findByMobileAndStatus(mobile,Account.STATUS_NORMAL);
        account.setPassword(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()));
        this.save(account);
    }

    /**
     * 检查激活信息是否可用，各个信息在数据库中是否有重复，执行激活前调用
     * @param userName 用户名
     * @param mobile 手机号
     * @param email 邮箱
     * @return
     */
    private boolean checkActiveInfo(String userName, String mobile, String email){
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
