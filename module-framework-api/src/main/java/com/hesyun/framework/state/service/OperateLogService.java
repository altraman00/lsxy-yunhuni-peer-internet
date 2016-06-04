package com.hesyun.framework.state.service;


import com.lsxy.framework.core.BaseService;
import com.lsxy.framework.core.security.SecurityUser;
import com.hesyun.framework.state.model.OperateLog;

/**
 * 用户登录日志业务层
 * @author lijing
 *
 */
public interface OperateLogService extends BaseService<OperateLog>{

	
	/**
	 * 添加操作log信息
	 * @param operateName
	 * @param remark
	 */
	public void addOperateLog(String operateName, String remark, SecurityUser user);
}
