package com.hesyun.framework.events.person;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Account;

/**
 * 删除用户事件
 * @author tandy
 *
 */
public class MQPersonDeletedEvent extends AbstractTenantEvent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Account account;
	
	public MQPersonDeletedEvent(Account person){
		this.account = person;
	}

	public Account getAccount() {
		return account;
	}
	

}