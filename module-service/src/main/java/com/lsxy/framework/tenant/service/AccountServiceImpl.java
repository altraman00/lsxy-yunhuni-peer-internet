package com.lsxy.framework.tenant.service;

import com.lsxy.framework.api.base.BaseDaoInterface;
import com.lsxy.framework.api.exceptions.AccountNotFoundException;
import com.lsxy.framework.api.exceptions.RegisterException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.api.tenant.model.Tenant;
import com.lsxy.framework.api.tenant.service.AccountService;
import com.lsxy.framework.api.tenant.service.TenantService;
import com.lsxy.framework.base.AbstractService;
import com.lsxy.framework.config.SystemConfig;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.DateUtils;
import com.lsxy.framework.core.utils.Page;
import com.lsxy.framework.core.utils.PasswordUtil;
import com.lsxy.framework.core.utils.UUIDGenerator;
import com.lsxy.framework.mq.api.MQService;
import com.lsxy.framework.mq.events.portal.RegisterSuccessEvent;
import com.lsxy.framework.tenant.dao.AccountDao;
import com.lsxy.yunhuni.api.apicertificate.model.ApiCertificate;
import com.lsxy.yunhuni.api.apicertificate.service.ApiCertificateService;
import com.lsxy.framework.api.billing.model.Billing;
import com.lsxy.framework.api.billing.service.BillingService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tandy on 2016/6/24.
 */
@Service
public class AccountServiceImpl extends AbstractService<Account> implements AccountService {


    public static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountDao accountDao;

    @Autowired
    private TenantService tenantService;

    @Autowired
    private ApiCertificateService apiCertificateService;

    @Autowired
    BillingService billingService;
    @Autowired
    private MQService mqService;


    @Override
    public BaseDaoInterface<Account, Serializable> getDao() {
        return accountDao;
    }


    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "account", key = "'account_' + #entity.userName", beforeInvocation = true),
                    @CacheEvict(value = "account", key = "'account_' + #entity.email", beforeInvocation = true),
                    @CacheEvict(value = "account", key = "'account_' + #entity.mobile", beforeInvocation = true),
                    @CacheEvict(value = "entity", key = "'entity_' + #entity.id", beforeInvocation = true)
            },
            put = {
                    @CachePut(value = "entity", key = "'entity_' + #entity.id",unless = "#entity == null")
            }
    )
    public Account save(Account entity) {
        return getDao().save(entity);
    }

    @Override
    public Account findPersonByLoginNameAndPassword(String userLoginName, String password) throws MatchMutiEntitiesException,AccountNotFoundException {
        String hql = "from Account obj where (obj.userName=?1 or obj.email=?2 or obj.mobile=?3) and obj.status=?4";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName,Account.STATUS_NORMAL);
        if(account != null) {
            if(!account.getPassword().equals(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()))){
                 account = null;
            }
        }else{
            throw new AccountNotFoundException("找不到账号");
        }
        return account;
    }

    @Override
    public Account findAccountByLoginName(String userLoginName) throws MatchMutiEntitiesException {
        String hql = "from Account obj where (obj.userName=?1 or obj.email=?2 or obj.mobile=?3) and obj.status=?4";
        Account account = this.findUnique(hql, userLoginName,userLoginName,userLoginName,Account.STATUS_NORMAL);
        return account;
    }

    @Override
    @Cacheable(value="account",key = "'account_'+#userName" ,unless="#result == null")
    public Account findAccountByUserName(String userName) {

        Account account = accountDao.findByUserNameAndStatus(userName,Account.STATUS_NORMAL);

        if (logger.isDebugEnabled()){
                logger.debug("-> findAccountByUserName In DB {} result: {}",userName,account);
         }

        return account;
    }

    @Override
    public int checkRegInfo(String userName, String mobile, String email){
        /*
            检查注册重复信息时，各项信息不能和已经激活的用户相同，同一信息若没激活，在注册的72小内也不能重复注册
         */
        long expireTime = Long.parseLong(SystemConfig.getProperty("account.email.expire","72"));
        Date limitTime = new Date(System.currentTimeMillis() - expireTime * 60 * 60 * 1000);

        //验证用户名是否能注册
        String userNameHql = "from Account obj where (obj.userName=?1 and obj.status in (?2,?3,?4)) or (obj.userName=?5 and obj.status=?6 and obj.createTime > ?7)";
        long userNameCount = countByCustom(userNameHql, userName, Account.STATUS_NORMAL,Account.STATUS_ABNORMAL,Account.STATUS_LOCK, userName, Account.STATUS_NOT_ACTIVE, limitTime);
        if(userNameCount > 0){
            return AccountService.REG_CHECK_USERNAME_EXIST;
        }
        //验证手机号是否能注册
        String mobileHql = "from Account obj where (obj.mobile=?1 and obj.status in (?2,?3,?4)) or (obj.mobile=?5 and obj.status=?6 and obj.createTime > ?7)";
        long mobileCount = countByCustom(mobileHql, mobile, Account.STATUS_NORMAL,Account.STATUS_ABNORMAL,Account.STATUS_LOCK, mobile, Account.STATUS_NOT_ACTIVE, limitTime);
        if(mobileCount > 0){
            return AccountService.REG_CHECK_MOBILE_EXIST;
        }
        //验证邮箱是否能注册
        String emailHql = "from Account obj where (obj.email=?1 and obj.status in (?2,?3,?4)) or (obj.email=?5 and obj.status=?6 and obj.createTime > ?7)";
        long emailCount = countByCustom(emailHql, email, Account.STATUS_NORMAL,Account.STATUS_ABNORMAL,Account.STATUS_LOCK, email, Account.STATUS_NOT_ACTIVE, limitTime);
        if(emailCount > 0){
            return AccountService.REG_CHECK_EMAIL_EXIST;
        }
        return AccountService.REG_CHECK_PASS;
    }

    @Override
    public Account createAccount(String userName, String mobile, String email) {

        int result = this.checkRegInfo(userName,mobile,email);
        if(logger.isDebugEnabled()){
            logger.debug("checkRegInfo：{} "+result);
        }
        if(result == AccountService.REG_CHECK_PASS){
            Account account = new Account();
            account.setUserName(userName);
            account.setMobile(mobile);
            account.setEmail(email);
            account.setStatus(Account.STATUS_NOT_ACTIVE);
            this.save(account);
            if(logger.isDebugEnabled()){
                logger.debug("RegisterSuccessEvent-start;{}"+account);
            }
            RegisterSuccessEvent event = new RegisterSuccessEvent(account.getId());
            mqService.publish(event);
            if(logger.isDebugEnabled()){
                logger.debug("RegisterSuccessEvent-end;{},{}"+account,event);
            }
            return account;
        }else{
            return null;

        }



    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "entity", key = "'entity_' + #accountId", beforeInvocation = true)
            }
    )
    public Account activeAccount(String accountId, String password) throws RegisterException{
        Account account = this.findById(accountId);
        //账号是否是未激活状态
        if(account != null && Account.STATUS_NOT_ACTIVE == account.getStatus()&& StringUtils.isNotBlank(password)){
            //校验激活信息是否重复
            boolean flag = this.checkActiveInfo(account.getUserName(),account.getMobile(),account.getEmail());
            if(flag){
                //创建租户
                Tenant tenant = tenantService.createTenant(account);
                //修改激活状态,设置密码
                account.setTenant(tenant);
                account.setStatus(Account.STATUS_NORMAL);
                account.setPassword(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()));
                account.setMm(PasswordUtil.desencode(password));
                this.save(account);
                //创建租户
//                Tenant tenant = account.getTenant();
                //创建主鉴权账号
                createApiCertificate(tenant);
                //帐务数据创建
                createBilling(tenant.getId());
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

    //创建主鉴权账号
    private void createApiCertificate(Tenant tenant) {
        ApiCertificate cert = new ApiCertificate();
        cert.setTenant(tenant);
        cert.setCertId(UUIDGenerator.uuid());
        cert.setSecretKey(UUIDGenerator.uuid());
        apiCertificateService.save(cert);
    }
    //帐务数据创建
    private void createBilling(String tenantId) {
        long defaultSize = Long.parseLong(SystemConfig.getProperty("portal.voiceflieplay.maxsize"))*1024*1024;
        Billing billing = new Billing();
        billing.setTenantId(tenantId);
        billing.setBalance(new BigDecimal(0.00));
        billing.setSmsRemain(0L);
        billing.setVoiceRemain(0L);
        billing.setConferenceRemain(0L);
        billing.setFileTotalSize(defaultSize);
        billing.setFileRemainSize(defaultSize);
        //由于余额计算的规则限制，把结算日期设为前一天
        billing.setBalanceDate(DateUtils.getPreDate(new Date()));
        billingService.save(billing);
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
        account.setMm(PasswordUtil.desencode(password));
        this.save(account);
    }

    @Override
    public void resetPwdByMobile(String mobile, String password) {
        Account account = accountDao.findByMobileAndStatus(mobile,Account.STATUS_NORMAL);
        account.setPassword(PasswordUtil.springSecurityPasswordEncode(password,account.getUserName()));
        account.setMm(PasswordUtil.desencode(password));
        this.save(account);
    }

    @Override
    public void cleanExpireRegisterAccount() {
        long expireTime = Long.parseLong(SystemConfig.getProperty("account.email.expire","72"));
        Date limitTime = new Date(System.currentTimeMillis() - expireTime * 60 * 60 * 1000);
        accountDao.cleanExpireRegisterAccount(Account.STATUS_EXPIRE,Account.STATUS_NOT_ACTIVE,limitTime);
    }

    @Override
    public List<Account> list() {
        String hql = " from Account obj where obj.status=?1 ";
        List<Account> list =  this.list(hql,Account.STATUS_NORMAL);
        return list;
    }

    @Override
    public boolean updateStatusByTenantId(String tenanId,Integer status) {
        List<Account> accs = accountDao.findByTenantId(tenanId);
        for (Account a : accs) {
            a.setStatus(status);
            this.save(a);
        }
        return true;
    }

    /**
     * 检查激活信息是否可用，各个信息在数据库中是否有重复（已激活的账号），执行激活前调用
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


    public Account findOneByTenant(String tenantId){
        String hql = "from Account obj where obj.tenant.id=?1";
        List<Account> accounts = this.findByCustomWithParams(hql, tenantId);
        if(accounts != null && accounts.size()>0){
            return accounts.get(0);
        }
        return null;
    }

    @Override
    public List<Account> findByStatus(Integer status) {
        int pageNo = 1;
        int pageSize = 20;
        List<Account> resultList = new ArrayList<>();
        Page<Account> accountPage = this.pageList(pageNo,pageSize);
        resultList.addAll(accountPage.getResult());
        while(accountPage.getCurrentPageNo() < accountPage.getTotalPageCount()){
            pageNo = (int)accountPage.getCurrentPageNo() + 1;
            accountPage = this.pageList( pageNo ,pageSize);
            resultList.addAll(accountPage.getResult());
        }
//        return accountDao.findByStatus(status);
        return resultList;
    }

    @Override
    public Account findByEmailAndStatus(String email, int status) {
        return accountDao.findByEmailAndStatus(email,status);
    }

    @Override
    public Page<Account> pList(Integer status, Integer pageNo, Integer pageSize) {
        String hql = " from Account obj where obj.status=?1 order by obj.createTime desc";
        return this.pageList(hql,pageNo,pageSize,status);
    }
}
