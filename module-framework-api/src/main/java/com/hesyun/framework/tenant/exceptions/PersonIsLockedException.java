package com.hesyun.framework.tenant.exceptions;

import com.hesyun.framework.tenant.model.Account;

public class PersonIsLockedException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PersonIsLockedException(Account person){
		super("商户"+person.getTenant().getTenantName()+"用户："+person.getUserName()+"已经被锁定");
	}

}