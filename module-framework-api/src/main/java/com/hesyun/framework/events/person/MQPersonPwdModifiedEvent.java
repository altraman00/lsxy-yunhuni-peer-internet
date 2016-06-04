package com.hesyun.framework.events.person;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户密码修改事件
 * @author tandy
 *
 */
public class MQPersonPwdModifiedEvent extends AbstractTenantEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account account;
	
	public MQPersonPwdModifiedEvent(Account person){
		this.account = person;
	}

	public Account getAccount() {
		return account;
	}


}
