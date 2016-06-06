package com.hesyun.framework.tenant.service;

import com.hesyun.framework.tenant.model.Account;
import com.lsxy.framework.core.BaseService;
import com.lsxy.framework.core.security.SecurityUser;

import java.util.List;

public interface AccountService extends BaseService<Account> {

	/**
	 * 制定手机号是否已经被注册
	 * @param mobile 指定手机号
	 * @return 被注册 true 否则 false
     */
	boolean isMobileExist(String mobile);

	/**
	 * 根据登录名获取用户对象标识
	 * 
	 * @param loginName
	 *            loginName可以是email，也可以是userName
	 * @return
	 */
	Account findAccountByLoginName(String loginName);

	/**
	 * 判断指定的属性对应的值是否已经存在数据,用于唯一性验证
	 * 
	 * @param fieldName
	 * @param value
	 * @return
	 */

	boolean isPropertyValueExist(String fieldName, String value);

	String generatUniqueLoginName(String chineseName);

	/**
	 * 获取和声云的管理员账号列表
	 * 
	 * @return
	 */
	List<Account> findHsyAdmins();

	/**
	 * 根据租户标识，查找该租户下的商户管理员
	 */
	List<Account> findTenantAdmins(String tenantUn);

	/**
	 * 根据openid获取用户对象 允许同一个openid可以挂在多个租户下面
	 * 
	 * @param openid
	 * @return
	 */
	List<Account> findPersonByWeixinOpenid(String openid,
										   boolean excludeHesongPerson);

	/**
	 * 根据openid获取用户对象 允许同一个openid可以挂在多个租户下面
	 * 
	 * @param openid
	 * @return
	 */
	List<Account> findPersonByWeixinOpenid(String openid);

	/**
	 * 根据工号获取用户对象
	 * 
	 * @param number
	 * @return
	 */
	Account findPersonByNumberOfTheTenant(String number, String tenantid);

	/**
	 * 根据邮箱获取用户对象
	 * 
	 * @return
	 */
	Account findPersonByEmail(String email);

	/**
	 * 根据租户表示获取租户用户 本来该方法可以不用的，但是由于tenant下面的tenantUsers是一个lazy加载的集合
	 * 无法直接在jsp环境中使用，所以写一个jsp中可以使用的获取租户用户的方法
	 * 
	 * @param tenantid
	 * @return 租户用户列表
	 */
	List<Account> findTenantUsersByTenantId(String tenantid);
	 
	List<Account> findTenantByTenantUn(String tenantUn);

	/**
	 * 获取商户管理员意外所有客服     
	 * 
	 * @param tenantUn
	 * @return
	 */
	List<Account> findUsersExceptAdmin(String tenantUn);


	/**
	 * 根据租户ID获取租户管理员
	 */
	List<Account> findTenantAdminByTenantId(String tenantid);

	/**
	 * 根据用户ID列表获取用户
	 */
	List<Account> findByIds(List<String> ids);

	/**
	 * 根据微信unionid查询用户对象
	 * 
	 * @param unionid
	 * @return
	 */
	Account findPersonByWxUnionId(String unionid);

	Account findByUserName(String userName);

	String findPersonNumAndNameByTenantid(String tenantid);

	Account findPersonByTenantAndName(String tenantid, String name);

	Account findPersonByNumOrName(String tenantid, String person);

	Account findPersonByName(String tenantid, String name);
	/**
	 * 判断指定的person是否是管理员角色
	 * @param person
	 * @return
	 */
	boolean isPersonAdministrator(Account person);


	Account findBySortNo(long sortNo);

	List<Account> findPersonByUsernameAndEmail(String username, String email);

	void batchDeleteByTenantId(String tenantId);

	Account loginVerify(String email, String password);

	List<String> findRoleOfAccount( String accountId);

	/**
	 * 根据登录用户名构建登录用户并返回
	 * @param username
	 * @return
	 */
	SecurityUser buildSecurityUserInUserName(String username);
}
