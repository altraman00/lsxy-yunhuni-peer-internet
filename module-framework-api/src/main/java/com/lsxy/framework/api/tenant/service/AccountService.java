package com.lsxy.framework.api.tenant.service;

import com.lsxy.framework.api.base.BaseService;
import com.lsxy.framework.api.tenant.model.Account;
import com.lsxy.framework.core.exceptions.MatchMutiEntitiesException;

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
    Account findPersonByLoginNameAndPassword(String userLoginName, String password) throws MatchMutiEntitiesException;

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
     * @throws MatchMutiEntitiesException
     */
    Account findAccountByUserName(String userName) throws MatchMutiEntitiesException;

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
     * @return
     */
    Account activeAccount(String accountId,String password);
}
