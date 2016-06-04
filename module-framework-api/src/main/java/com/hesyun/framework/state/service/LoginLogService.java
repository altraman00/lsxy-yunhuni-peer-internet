package com.hesyun.framework.state.service;


import com.lsxy.framework.core.BaseService;
import com.hesyun.framework.state.model.LoginLog;

/**
 * 用户登录日志业务层
 * @author tandy
 *
 */
public interface LoginLogService extends BaseService<LoginLog>{

	
	/**
	 * 根据用户Id获取用户最后一次登录记录
	 */
	public LoginLog getLastLogin(String personId);
	
	/**
	 * 获取用户最新修改密码日志
	 * @param account
	 * @return
	 */
	public boolean checkPasswordPeriod(String account);
}
