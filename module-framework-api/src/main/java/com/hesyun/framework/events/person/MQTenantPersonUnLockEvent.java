package com.hesyun.framework.events.person;

import com.hesyun.framework.events.AbstractTenantEvent;
import com.hesyun.framework.tenant.model.Account;

/**
 * 用户锁定事件
 * @author tandy
 *
 */
public class MQTenantPersonUnLockEvent extends AbstractTenantEvent {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private Account kefur;
	
	public MQTenantPersonUnLockEvent(Account person){
		this.kefur = person;
	}

	public Account getPerson() {
		return kefur;
	}
}
