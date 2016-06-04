package com.hesyun.framework.events.person;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户锁定事件
 * @author tandy
 *
 */
public class MQTenantPersonLockEvent extends AbstractTenantEvent {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String username;
	
	public MQTenantPersonLockEvent(Account person){
		this.username = person.getUserName();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "MQTenantPersonLockEvent [username=" + username + "]";
	}

}
