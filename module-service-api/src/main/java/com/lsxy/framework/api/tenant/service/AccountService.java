package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.exceptions.AccountNotFoundException;
import com.lsxy.framework.api.exceptions.RegisterException;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;
import com.lsxy.framework.core.utils.Page;

import java.util.List;

public interface AccountService extends BaseService<Account> {
    int REG_CHECK_PASS = 0;             //账号注册信息检验通过
    int REG_CHECK_USERNAME_EXIST = 1;   //账号注册信息失败，用户名已存在
    int REG_CHECK_MOBILE_EXIST = 2;     //账号注册信息失败，手机已存在
    int REG_CHECK_EMAIL_EXIST = 3;      //账号注册信息失败，邮箱已存在

    /**
     * 根据登陆用户名和密码查询账号信息
     * @param userLoginName  用户登录名称可以是会员名  email 或手机号
     * @param password 用户密码  使用passwordutils.springpasswordencode方法并使用username作为盐值进行加密的密码
     * @return
     */
    Account findPersonByLoginNameAndPassword(String userLoginName, String password) throws MatchMutiEntitiesException,AccountNotFoundException;

    /**
     * 根据登陆用户名查询账号信息
     * @param userLoginName 用户登录名称可以是会员名  email 或手机号
     * @return
     */
    Account findAccountByLoginName(String userLoginName) throws MatchMutiEntitiesException;

    /**
     * 根据登陆用户名查询账号信息
     * @param userName 用户名
     * @return 账号信息
     */
    Account findAccountByUserName(String userName);

    /**
     * 用户注册-检查注册信息是否重复
     * @param userName 用户名
     * @param mobile 手机
     * @param email 邮箱
     * @return 返回数据说明参考AccountService.REG_CHECK_*
     */
    int checkRegInfo(String userName, String mobile, String email);

    /**
     * 用户注册-保存用户信息
     * @param userName
     * @param mobile
     * @param email
     * @return
     */
    Account createAccount(String userName, String mobile, String email);

    /**
     * 用户注册-激活用户
     * @param accountId 激活账号ID
     * @param password 密码
     * @return RegisterException 若激活失败，则抛出此异常
     */
    Account activeAccount(String accountId,String password) throws RegisterException;

    /**
     * 忘记密码-检查邮箱是否存在
     * @param email 邮箱
     */
    boolean checkEmail(String email);

    /**
     * 忘记密码-检查手机是否存在
     * @param mobile 邮箱
     */
    boolean checkMobile(String mobile);

    /**
     * 根据邮箱修改密码
     * @param email 邮箱
     * @param password 密码
     */
    void resetPwdByEmail(String email, String password);

    /**
     * 根据手机箱修改密码
     * @param mobile 手机
     * @param password 密码
     */
    void resetPwdByMobile(String mobile, String password);

    /**
     * 清除过期的注册账号（用于定时任务）
     */
    void cleanExpireRegisterAccount();

    boolean updateStatusByTenantId(String tenanId,Integer status);


    /**
     * 获取租户的账号信息
     * @param tenantId
     * @return
     */
    public Account findOneByTenant(String tenantId);

    /**
     * 根据状态获取用户集合
     * @param status
     * @return
     */
    List<Account> findByStatus(Integer status);

    /**
     * 根据邮箱查找账号
     * @param email 邮箱
     * @param status 状态
     * @return
     */
    Account findByEmailAndStatus(String email,int status);

    /**
     * 根据状态获取用户集合
     * @param status
     * @return
     */
    Page<Account> pList(Integer status, Integer pageNo, Integer pageSize);
}
