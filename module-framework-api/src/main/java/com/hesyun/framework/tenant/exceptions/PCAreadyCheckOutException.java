package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

/**
 * PC端已经处于签出状态异常
 * @author tandy
 *
 */
public class PCAreadyCheckOutException extends Exception {

	public PCAreadyCheckOutException(Account person) {
		super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已经处于签出状态了");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
