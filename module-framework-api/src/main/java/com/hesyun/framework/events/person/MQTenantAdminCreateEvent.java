package com.hesyun.framework.events.person;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户的管理员身份改变事件
 * @author tandy
 *
 */
public class MQTenantAdminCreateEvent extends AbstractTenantEvent {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Account person;
	
	public MQTenantAdminCreateEvent(Account person){
		this.person = person;
	}

	public Account getPerson() {
		return person;
	}
}
