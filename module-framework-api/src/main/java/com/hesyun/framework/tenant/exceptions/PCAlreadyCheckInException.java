package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

/**
 * PC端座席已经签到异常
 * @author tandy
 *
 */
public class PCAlreadyCheckInException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PCAlreadyCheckInException(Account person){
		super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已经处于签到状态");
	}

}
